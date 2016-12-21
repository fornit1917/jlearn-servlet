package jlearn.servlet.service;

import jlearn.servlet.dto.Book;
import jlearn.servlet.dto.BookDetails;
import jlearn.servlet.service.utility.Pair;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BookDetailsService
{
    public CompletableFuture<BookDetails> loadDetails(Book book) throws UnsupportedEncodingException
    {
        AsyncHttpClient httpClient = new DefaultAsyncHttpClient();
        CompletableFuture<BookDetails> result = new CompletableFuture<>();

        String query = book.getFullName();
        String searchUrl = "http://www.e-reading.club/?query=" + URLEncoder.encode(query, "Windows-1251");
        httpClient.prepareGet(searchUrl).execute().toCompletableFuture()
                .thenCompose(response -> {
                    ArrayList<Pair<String, String>> searchResult = parseSearchResult(response.getResponseBody(Charset.forName("Windows-1251")));
                    if (searchResult == null || searchResult.isEmpty()) {
                        CompletableFuture<Response> emptyResult = new CompletableFuture<>();
                        emptyResult.complete(null);
                        return emptyResult;
                    } else {
                        String bookUrl = getUrlForBestSearchResult(searchResult, query);
                        return httpClient.prepareGet(bookUrl).execute().toCompletableFuture();
                    }
                })
                .thenAccept(response -> {
                    BookDetails bookDetails;
                    if (response == null) {
                        bookDetails = null;
                    } else {
                        bookDetails = parseBookPage(response.getResponseBody(Charset.forName("Windows-1251")));
                    }
                    try {
                        httpClient.close();
                    } catch (IOException e) {
                        //todo: log
                    }
                    result.complete(bookDetails);
                })
                .exceptionally(e -> {
                    result.completeExceptionally(e);
                    return null;
                });

        return result;
    }

    private ArrayList<Pair<String, String>> parseSearchResult(String html)
    {
        ArrayList<Pair<String, String>> result = new ArrayList<>(10);
        Document doc = Jsoup.parse(html);
        Elements records = doc.select(".bookrecord");

        int n = records.size() > 10 ? 10 : records.size();
        for (int i = 0; i < n; i++) {
            Element record = records.get(i);
            Elements links = record.select("a");
            if (links.size() < 2) {
                continue;
            }
            String value = links.get(0).text() + " " + links.get(1).text();
            String url = "http://www.e-reading.club" + links.get(1).attr("href");
            result.add(new Pair<>(value, url));
        }

        return result;
    }

    private String getUrlForBestSearchResult(ArrayList<Pair<String, String>> searchResult, String query)
    {
        ArrayList<String> queryWords = Arrays.stream(query.split("\\s+"))
            .map(s -> s.toLowerCase())
            .collect(Collectors.toCollection(ArrayList::new));

        int best = 0;
        int bestIndex = 0;
        for (int i = 0; i < searchResult.size(); i++) {
            String value = searchResult.get(i).getFirst().toLowerCase();
            int count = 0;
            for (int j = 0; j < queryWords.size(); j++) {
                if (value.indexOf(queryWords.get(j)) != -1) {
                    count += queryWords.get(j).length();
                }
            }
            if (count > best) {
                best = count;
                bestIndex = i;
            }
        }
        return searchResult.get(bestIndex).getSecond();
    }

    private BookDetails parseBookPage(String html)
    {
        BookDetails bookDetails = new BookDetails();
        Document doc = Jsoup.parse(html);

        Elements rows = doc.select("table table table tr");
        for (Element row: rows) {
            Elements tds = row.select("td");
            switch (tds.get(0).text().trim()) {
                case "Автор:":
                    bookDetails.setAuthor(tds.get(1).text());
                    break;
                case "Название:":
                    bookDetails.setTitle(tds.get(1).text());
                    break;
                case "Жанр:":
                    bookDetails.setGenre(tds.get(1).text());
                    break;
                case "Описание:":
                    bookDetails.setAnnotation(tds.get(1).html());
                    break;
                case "Издание:":
                    bookDetails.setYear(tds.get(1).text());
                    break;
            }
        }

        Elements bs = doc.select("table > tbody > tr > td > b");
        for (Element b: bs) {
            String t = b.text();
            if (t.indexOf("Полный текст книги") != -1) {
                String readLink = b.select("a").get(0).attr("href");
                bookDetails.setReadLink("http://e-reading.club/" + readLink);
            } else if (t.indexOf("Скачать эту книгу") != -1) {
                Elements links = b.select("a");
                for (Element link: links) {
                    bookDetails.getDownloadLinks().put(link.text().trim(), "http://e-reading.club/" + link.attr("href"));
                }
            }
        }

        return bookDetails;
    }
}
