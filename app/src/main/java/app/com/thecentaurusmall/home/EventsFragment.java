package app.com.thecentaurusmall.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.Query;

import app.com.thecentaurusmall.databinding.EventItemBinding;
import app.com.thecentaurusmall.databinding.EventsFragmentBinding;
import app.com.thecentaurusmall.home.viewmodels.EventsViewModel;
import app.com.thecentaurusmall.model.Event;

public class EventsFragment extends Fragment {

    private EventsViewModel mViewModel;
    //    private ;
    private PagedList.Config mPagedListConfig;
    private EventsFragmentBinding mEventFragmentBinding;

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mEventFragmentBinding = EventsFragmentBinding.inflate(inflater, container, false);
        return mEventFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EventsViewModel.class);

        mPagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(3)
                .setPageSize(3)
                .build();

        mEventFragmentBinding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestorePagingOptions<Event> options = new FirestorePagingOptions.Builder<Event>()
                .setLifecycleOwner(EventsFragment.this)
                .setQuery(mViewModel.getNextWeekEvents(), mPagedListConfig, Event.class)
                .build();

        setUpAdapter(options);


        // The options for the adapter combine the paging configuration with query information
        // and application-specific options for lifecycle, etc.

        mEventFragmentBinding.datesChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Log.d("Chip group listener", String.valueOf(chipGroup.getCheckedChipId()) + i);
                
                String message = null;
                int id = chipGroup.getCheckedChipId();
                Query baseQuery = null;
                switch (id) {
                    case 1:
                        message = "Last Month";
                        baseQuery = mViewModel.geLastMonthEvents();
                        break;
                    case 2:
                        message = "Last Week";
                        baseQuery = mViewModel.getLastWeekEvents();
                        break;
                    case 3:
                        message = "Today";
                        baseQuery = mViewModel.getTodayEvents();
                        break;
                    case 4:
                        message = "Next Week";
                        baseQuery = mViewModel.getNextWeekEvents();
                        break;
                    case 5:
                        message = "Next Month";
                        baseQuery = mViewModel.getNextMonthEvents();
                        break;
                    default:
                        baseQuery = mViewModel.getNextWeekEvents();
                        break;
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                FirestorePagingOptions<Event> options = new FirestorePagingOptions.Builder<Event>()
                        .setLifecycleOwner(EventsFragment.this)
                        .setQuery(baseQuery, mPagedListConfig, Event.class)
                        .build();

                setUpAdapter(options);

            }
        });

    }


    private void setUpAdapter(FirestorePagingOptions<Event> options) {


        FirestorePagingAdapter<Event, EventViewHolder> mPagingAdapter =
                new FirestorePagingAdapter<Event, EventViewHolder>(options) {
                    @NonNull
                    @Override
                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
                        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                        final EventItemBinding binding = EventItemBinding.inflate(layoutInflater, parent, false);
                        return new EventViewHolder(getContext(), binding);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EventViewHolder holder,
                                                    int position,
                                                    @NonNull Event model) {

                        holder.performBind(model);
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull LoadingState state) {
                        switch (state) {
                            case LOADING_INITIAL:
                            case LOADING_MORE:
//                                mProgressBar.setVisibility(View.VISIBLE);
                                break;
                            case LOADED:
//                                mProgressBar.setVisibility(View.GONE);
                                break;
                            case FINISHED:
//                                mProgressBar.setVisibility(View.GONE);
//                                showToast("Reached end of data set.");
                                break;
                            case ERROR:
//                                showToast("An error occurred.");
                                retry();
                                break;
                        }
                    }
                };
        mEventFragmentBinding.eventsRecyclerView.setAdapter(mPagingAdapter);

    }

}
