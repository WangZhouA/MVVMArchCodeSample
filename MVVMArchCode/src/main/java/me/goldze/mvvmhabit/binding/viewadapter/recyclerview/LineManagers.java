package me.goldze.mvvmhabit.binding.viewadapter.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by goldze on 2017/6/16.
 */
public class LineManagers {
    protected LineManagers() {
    }

    public interface LineManagerFactory {
        RecyclerView.ItemDecoration create(RecyclerView recyclerView);
    }


    public static LineManagerFactory both() {
        return recyclerView -> new DividerLine(recyclerView.getContext(), DividerLine.LineDrawMode.BOTH);
    }

    public static LineManagerFactory horizontal() {
        return recyclerView -> new DividerLine(recyclerView.getContext(), DividerLine.LineDrawMode.HORIZONTAL);
    }

    public static LineManagerFactory vertical() {
        return recyclerView -> new DividerLine(recyclerView.getContext(), DividerLine.LineDrawMode.VERTICAL);
    }
}
