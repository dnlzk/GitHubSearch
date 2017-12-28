package pl.nalazek.githubsearch;

import android.view.View;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Daniel Nalazek
 */

public class ProgressBarManager implements Observer {

    private View progressBar;

    public ProgressBarManager(View progressBar) {
        this.progressBar = progressBar;
    }

    public void setProgressBarVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setProgressBarUnvisible() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void update(Observable observable, Object data) {
        setProgressBarUnvisible();
    }
}
