package jlearn.servlet.dto;

import java.util.HashMap;
import java.util.Map;

public class BookDetails
{
    private String author = "";
    private String title = "";
    private String year = "";
    private String genre = "";
    private String annotation = "";
    private String readLink;
    private Map<String, String> downloadLinks;

    public BookDetails()
    {
        downloadLinks = new HashMap<>();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getReadLink() {
        return readLink;
    }

    public void setReadLink(String readLink) {
        this.readLink = readLink;
    }

    public Map<String, String> getDownloadLinks() {
        return downloadLinks;
    }
}
