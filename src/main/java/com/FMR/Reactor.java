package com.FMR;

@SuppressWarnings("unused")
public class Reactor {
    //The following are not supposed to change
    private final String _name;
    private final String _model;
    private final int _MWt;
    private final int _MWe;

    //The following is supposed to change
    private final int[] _powerHistory; //First is the most recent, stores in %

    public Reactor(String name, String model, int MWt, int MWe)
    {
        _name = name;
        _model = model;
        _MWt = MWt;
        _MWe = MWe;
        _powerHistory = new int[6];
    }

    public void setNewPower(int newPower)
    {
        for (int i = _powerHistory.length - 2; i > 0; i--)
        {
            //shifts everything back because this stores history
            _powerHistory[i + 1] = _powerHistory[i];
        }
        _powerHistory[0] = newPower;
    }

    /**
     * Useful when you add a new reactor card.
     * Ignores any entries past index 4.
     * @param data <code>int[]</code> The data we will fill into the reactor.
     * @throws IllegalArgumentException Will except if data is too short, making us unable to fill out the reactor.
     */
    public void fillInData(int[] data)
    throws IllegalArgumentException
    {
        if (data.length < 4) { throw new IllegalArgumentException("Not enough historical data for reactor " + _name); }
        System.arraycopy(data, 0, _powerHistory, 0, _powerHistory.length);
    }

    public String get_name()
    {
        return _name;
    }

    public String get_model()
    {
        return _model;
    }

    public int get_MWt()
    {
        return _MWt;
    }

    public int get_MWe()
    {
        return _MWe;
    }

    public int[] get_powerHistory()
    {
        return _powerHistory;
    }
}
