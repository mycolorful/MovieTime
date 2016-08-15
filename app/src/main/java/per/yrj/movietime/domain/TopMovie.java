package per.yrj.movietime.domain;

/**
 * Created by YiRenjie on 2016/6/1.
 */
public class TopMovie extends Movie{
    int box;
    int rank;

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
