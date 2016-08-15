package per.yrj.movietime.domain;

/**
 * Created by YiRenjie on 2016/5/31.
 */
public class Movie {
    String detailUrl;
    String posterUrl;
    String title;
    String rating;

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "detailUrl='" + detailUrl + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
