package pl.nalazek.githubsearch;

import android.view.View;

import java.util.Observable;
import java.util.Observer;

/**
 * Used to manage progress bar visibility. Implements Observer interface to listen for changes on SearchQuery Tasks
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
        QueryHistory queryHistory = (QueryHistory) observable;
        QueryTask queryTask = (QueryTask) data;
        ResponsePackage responsePackage = queryHistory.get(queryTask);
        if(!responsePackage.getMessage().equals("Task interrupted"))
            setProgressBarUnvisible();
    }
}
