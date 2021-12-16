package ma.ensaf.veryempty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;
import java.util.Objects;

import ma.ensaf.veryempty.R;
import ma.ensaf.veryempty.adapters.viewholders.HeaderViewHolder;
import ma.ensaf.veryempty.adapters.viewholders.RowViewHolder;
import ma.ensaf.veryempty.databinding.ItemDonorsHeaderBinding;
import ma.ensaf.veryempty.databinding.ItemDonorsRowBinding;
import ma.ensaf.veryempty.models.HeaderItem;
import ma.ensaf.veryempty.models.RowItem;
import ma.ensaf.veryempty.models.UsersListItem;
import ma.ensaf.veryempty.utils.DateTimeUtils;

public class DonorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UsersListItem> filtered_items;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, UsersListItem obj);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DonorsAdapter(Context ctx, List<UsersListItem> items) {
        this.ctx = ctx;
        filtered_items = items;
    }

    public void setUsersListItemList(List<UsersListItem> items) {
        // then update the items
        filtered_items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case UsersListItem.TYPE_HEADER:
                ItemDonorsHeaderBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_donors_header, parent, false);
                viewHolder = new HeaderViewHolder(binding);
                break;
            case UsersListItem.TYPE_ROW:
                ItemDonorsRowBinding rowBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_donors_row, parent, false);
                viewHolder = new RowViewHolder(rowBinding);
                break;
        }

        return Objects.requireNonNull(viewHolder);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case UsersListItem.TYPE_HEADER:
                HeaderItem headerItem = (HeaderItem) filtered_items.get(position);
                HeaderViewHolder hViewHolder = (HeaderViewHolder)(holder);

                // Populate date item data here
                String dateString = DateTimeUtils.parseDateTime(headerItem.getDate(), "yyyy-MM-dd", "dd MMM yyyy");
                hViewHolder.binding.donationDateTextView.setText(ctx.getString(R.string.last_donation_date, dateString));
                break;
            case UsersListItem.TYPE_ROW:
                RowItem rowItem = (RowItem) filtered_items.get(position);
                RowViewHolder rViewHolder = (RowViewHolder)(holder);
                // Populate date item data here
                rViewHolder.binding.userNameTextView.setText(rowItem.getUsers().getName());
                rViewHolder.binding.userImageView.setImageResource(rowItem.getUsers().getImage());
                rViewHolder.binding.userLocationTextView.setText(rowItem.getUsers().getLocation());
                rViewHolder.binding.bloodGroupTextView.setText(rowItem.getUsers().getBloodGroup());

                // click listeners
                rViewHolder.binding.askForHelpTextView.setOnClickListener(view -> {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, position, rowItem);
                    }
                });

                break;
        }

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private int lastPosition = -1;
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (filtered_items != null) {
            return filtered_items.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return filtered_items.get(position).getType();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public UsersListItem getItem(int position) {
        return filtered_items.get(position);
    }
}
