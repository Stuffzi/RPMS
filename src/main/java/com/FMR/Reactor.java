package com.FMR;

public class Reactor {
    private String _name;
    private String _model;
    private int _MWt;
    private int _MWe;
    private int[] _powerHistory; //First is the most recent

    public Reactor(String name, String model, int MWt, int MWe)
    {

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
