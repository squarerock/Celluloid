package squarerock.celluloid.model;

/**
 * Created by pranavkonduru on 10/15/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Trailers {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailerResults> results = new ArrayList<>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The results
     */
    public List<TrailerResults> getTrailers() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<TrailerResults> results) {
        this.results = results;
    }

}