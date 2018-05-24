package com.example.converter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    public String TAG = MainActivity.class.getSimpleName();
    static String pages[] = {"TEMPERATURE", "TIME", "AREA", "VOLUME", "DISTANCE", "WEIGHT", "SPEED", "FUEL EFFICIENCY"};

    static String units[][] = { {"CELSIUS", "FAHRENHEIT", "KELVIN"},
            {"MICRO SECOND", "MILLI SECOND", "SECOND", "MINUTE"},
            {"SQUARE FEET", "SQUARE METRE", "SQUARE KM", "SQUARE MILE", "ACRE", "HECTARE"},
            {"MILLI LITRE", "LITRE", "PINT", "QUART", "GALLON"},
            {"METRE", "KILO METRE", "FEET", "YARD", "MILE"},
            {"GRAM", "KILO GRAM", "OUNCE", "POUND"},
            {"FOOT/SEC", "METRE/SEC", "KM/HOUR", "MILES/HOUR"},
            {"KM/LITRE", "MILES/GALLON"}
    };

    static String tempConv[][] = { {"x", "9*x/5 + 32", "x + 273.15"},
            {"(x - 32)*5/9", "x", "(x + 459.67)*5/9"},
            {"x - 273.15", "x*9/5 - 459.67", "x"}
    };

    static String timeConv[][] = { {"x", "x/1000", "x/1000000", "x/60000000"},
            {"1000*x", "x", "x/1000", "x/60000"},
            {"1000000*x", "1000*x", "x", "x/60"},
            {"60000000*x", "60000*x", "60*x", "x"}
    };

    static String areaConv[][] = { {"x", "x/10.764", "x*9.2903/100000000", "x*3.587/100000000", "x*2.2957/100000", "x*9.2903/1000000"},
            {"x*10.764", "x", "x/1000000", "x*3.861/10000000", "x*2.47/10000", "x/10000"},
            {"x*1.076*10000000", "x*1000000", "x", "x*0.386", "x*247.105", "x*100"},
            {"x*2.788*10000000", "x*2.59*1000000", "x*2.5899", "x", "x*640", "x*259"},
            {"x*43560", "x*4046.86", "x*4.04686/1000", "x*1.5625/1000", "x", "x*0.404686"},
            {"x*107639", "x*10000", "x/100", "x*3.86/1000", "x*2.47105", "x"}
    };

    static String volumeConv[][] = { {"x", "x/1000", "x*2.11338/1000", "x*1.05669/1000", "x*2.64172/10000"},
            {"x/1000", "x", "x*2.11338", "x*1.05669", "x*0.264172"},
            {"x*473.176", "x*0.473176", "x", "x/2", "x/8"},
            {"x*946.353", "x*0.946353", "x*2", "x", "x/4"},
            {"x*3785.41", "x*3.78541", "x*8", "x*4", "x"}
    };

    static String distanceConv[][] = { {"x", "x/1000", "x*3.28084", "x*1.09361", "x*6.21371/10000"},
            {"x*1000", "x", "x*3280.84", "x*1093.61", "x*0.621371"},
            {"x*0.3048", "x*3.048/10000", "x", "x/3", "x*1.89394/10000"},
            {"x*0.9144", "x*9.144/10000", "x*3", "x", "x*5.68182/10000"},
            {"x*1609.34", "x*1.60934", "x*5280", "x*1760", "x"}
    };

    static String weightConv[][] = { {"x", "x/1000", "x*0.035274", "x*0.00220462"},
            {"x*1000", "x", "x*35.274", "x*2.20462"},
            {"x*28.3495", "x*0.0283495", "x", "x*0.0625"},
            {"x*453.592", "x*0.453592", "x*16", "x"}
    };

    static String speedConv[][] = { {"x", "x*0.3048", "x*1.09728", "x*0.681818"},
            {"x*3.28084", "x", "x*3.6", "x*2.23694"},
            {"x*0.911344", "x*0.277778", "x", "x*0.621371"},
            {"x*1.46667", "x*0.44704", "x*1.60934", "x"}
    };

    static String fueleffConv[][] = { {"x", "x*2.8248"},
            {"x*0.354", "x"}
    };

    static String conversions[][][] = {tempConv, timeConv, areaConv, volumeConv, distanceConv, weightConv, speedConv, fueleffConv};

    static int x,y;
    static boolean xFocus, yFocus;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final int section = getArguments().getInt(ARG_SECTION_NUMBER);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(pages[section]);

            final EditText et1 = (EditText) rootView.findViewById(R.id.edit1);
            final EditText et2 = (EditText) rootView.findViewById(R.id.edit2);

            et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    xFocus = hasFocus;
                }
            });
            et1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!xFocus) {
                        return;
                    }
                    if (s.length() == 0) {
                        et2.setText("");
                        return;
                    }
                    Double val;
                    try{
                        val = Double.parseDouble(s.toString());
                    } catch (NumberFormatException e) {
                        return;
                    }
                    Expression e = new ExpressionBuilder(conversions[section][x][y])
                            .variables("x").build()
                            .setVariable("x", val);
                    et2.setText("" + e.evaluate());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    yFocus = hasFocus;
                }
            });
            et2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!yFocus) {
                        return;
                    }
                    if (s.length() == 0) {
                        et1.setText("");
                        return;
                    }
                    Double val;
                    try{
                        val = Double.parseDouble(s.toString());
                    } catch (NumberFormatException e) {
                        return;
                    }
                    Expression e = new ExpressionBuilder(conversions[section][y][x])
                            .variables("x").build()
                            .setVariable("x", val);
                    et1.setText("" + e.evaluate());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            Spinner spin1 = (Spinner) rootView.findViewById(R.id.spinner1);
            spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    x = position;

                    if (xFocus) {
                        if (et2.getText().length() == 0) {
                            et2.setText("");
                            return;
                        }
                        Double val;
                        try {
                            val = Double.parseDouble(et2.getText().toString());
                        } catch (NumberFormatException e) {
                            return;
                        }
                        Expression e = new ExpressionBuilder(conversions[section][y][x])
                                .variables("x").build()
                                .setVariable("x", val);
                        et1.setText("" + e.evaluate());
                    } else if (yFocus) {
                        if (et1.getText().length() == 0) {
                            et1.setText("");
                            return;
                        }
                        Double val;
                        try {
                            val = Double.parseDouble(et1.getText().toString());
                        } catch (NumberFormatException e) {
                            return;
                        }
                        Expression e = new ExpressionBuilder(conversions[section][x][y])
                                .variables("x").build()
                                .setVariable("x", val);
                        et2.setText("" + e.evaluate());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            x = spin1.getSelectedItemPosition();

            ArrayAdapter adapter1 = new ArrayAdapter(this.getContext(), R.layout.spinner_text, units[section]);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin1.setAdapter(adapter1);
            spin1.setSelection(0);

            Spinner spin2 = (Spinner) rootView.findViewById(R.id.spinner2);
            spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    y = position;

                    if (yFocus) {
                        if (et1.getText().length() == 0) {
                            et1.setText("");
                            return;
                        }
                        Double val;
                        try {
                            val = Double.parseDouble(et1.getText().toString());
                        } catch (NumberFormatException e) {
                            return;
                        }
                        Expression e = new ExpressionBuilder(conversions[section][x][y])
                                .variables("x").build()
                                .setVariable("x", val);
                        et2.setText("" + e.evaluate());
                    } else if (xFocus) {
                        if (et2.getText().length() == 0) {
                            et2.setText("");
                            return;
                        }
                        Double val;
                        try {
                            val = Double.parseDouble(et2.getText().toString());
                        } catch (NumberFormatException e) {
                            return;
                        }
                        Expression e = new ExpressionBuilder(conversions[section][y][x])
                                .variables("x").build()
                                .setVariable("x", val);
                        et1.setText("" + e.evaluate());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            y = spin2.getSelectedItemPosition();

            ArrayAdapter adapter2 = new ArrayAdapter(this.getContext(), R.layout.spinner_text, units[section]);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin2.setAdapter(adapter2);
            spin2.setSelection(0);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "PAGE" + position;
        }
    }
}
