package pl.nalazek.githubsearch.util;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * This class respresents the behavior of the RelativeLayout instace placed in the CoordinatorLayout
 * @author Daniel Nalazek
 */
public class LayoutWithListBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {

    private static Integer initialMenuPosition = 0;

    public LayoutWithListBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        if(initialMenuPosition==0) initialMenuPosition = dependency.getBottom();
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        if(dependency instanceof AppBarLayout) {
            AppBarLayout appBarLayout = (AppBarLayout) dependency;

            //calculate new position
            int positionDifference = dependency.getBottom()-initialMenuPosition;

            //set translation
            child.setTranslationY(positionDifference);
            return true;
        }
        return false;
    }
}
