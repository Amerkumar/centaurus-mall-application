package app.com.thecentaurusmall.home.viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;

import app.com.thecentaurusmall.MainRepository;

public class EventsViewModel extends ViewModel {

    private MainRepository mainRepository;

    public EventsViewModel() {
        mainRepository = new MainRepository();
    }

    public Query getTodayEvents() {
        return mainRepository.getTodayEvents();
    }

    public Query getNextWeekEvents() {
        return mainRepository.getNextWeekEvents();
    }

    public Query getNextMonthEvents() {
        return mainRepository.getNextMonthEvents();
    }

    public Query getLastWeekEvents() {
        return mainRepository.getLastWeekEvents();
    }

    public Query geLastMonthEvents() {
        return mainRepository.getLastMonthEvents();
    }

    public Query getPastEvents() { return mainRepository.getPastEvents();}

    public Query getUpcomingEvents() { return mainRepository.getUpcomingEvents(); }
}
