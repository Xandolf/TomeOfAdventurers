package com.at.gmail.tomeofadventurers.Classes;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

//This is a single instance of the Bus Class to be shared across all activities/fragments.
//The API suggests optimizing this by injecting it into specific classes.
// I will look into the necessity of it But for now this works

public class BusProvider
{
    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
    }
}
