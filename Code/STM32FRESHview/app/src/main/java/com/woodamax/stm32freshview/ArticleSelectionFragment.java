package com.woodamax.stm32freshview;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by maxim on 05.04.2017.
 */

public class ArticleSelectionFragment extends Fragment {
    DatabaseHelper myDBH;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.article_selection_fragment, container, false);

        Button articleButton = (Button) getActivity().findViewById(R.id.my_reading_article_button);
        Button submitButton = (Button) getActivity().findViewById(R.id.my_reading_submit_button);
        if(MainActivity.fh.getCenter().equals("Article_selection_Fragment")){
            articleButton.setVisibility(view.INVISIBLE);
            if(MainActivity.fh.isAuthor()){
                submitButton.setVisibility(view.INVISIBLE);
            }

        }
        //Toast.makeText(getContext(),MainActivity.fh.getCenter(), Toast.LENGTH_SHORT).show();
        //This uses an different design for the spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.micro_controllers, R.layout.my_spinner_item);
        final Spinner microControllerSpinner = (Spinner) view.findViewById(R.id.article_micro_controller_spinner);
        microControllerSpinner.setAdapter(adapter);

        //Preselect the article button on Readersview is pressed when article is pressed
        preselectArticleCategory(microControllerSpinner);
        //Click Listener for the items inside the spinner
        int selectionCurrent = microControllerSpinner.getSelectedItemPosition();
        microControllerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Select Controller")){
                    addArticlePreview("none");
                }
                if(selectedItem.equals("STM32F1")){
                    addArticlePreview("STM32F1");
                }
                if(selectedItem.equals("STM32F3")){
                    addArticlePreview("STM32F3");
                }
                if(selectedItem.equals("STM32F4")){
                    addArticlePreview("STM32F4");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void preselectArticleCategory(Spinner microControllerSpinner) {
        switch (MainActivity.fh.getControllerType()){
            case 1:
                microControllerSpinner.setSelection(1);
                break;
            case 3:
                microControllerSpinner.setSelection(2);
                break;
            case 4:
                microControllerSpinner.setSelection(3);
                break;
            default:
                microControllerSpinner.setSelection(0);
                break;
        }
    }

    private void addArticlePreview(String controller) {
        switch (controller){
            case "STM32F1":
                buildSelectionView(controller);
                break;
            case "STM32F3":
                buildSelectionView(controller);
                break;
            case "STM32F4":
                buildSelectionView(controller);
                break;
            default:
                buildSelectionView(controller);
                break;
        }
    }

    /**
     * Build the view to display the articles, correspondig to the spinner item
     * @param controller selected spinner item
     */
    private void buildSelectionView(String controller) {
        //This is the parentview
        ScrollView parentView = (ScrollView) getActivity().findViewById(R.id.article_selection_preview);
        LinearLayout childview = new LinearLayout(getActivity());
        //Because we build a new view, we have to define the xml parameters
        childview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        childview.setOrientation(LinearLayout.VERTICAL);
        //finally remove everything
        parentView.removeAllViews();
        myDBH = new DatabaseHelper(getActivity());
        Cursor res = myDBH.getArticleDescription();
        while (res.moveToNext()) {
            if(res.getString(1).contains(controller)){
                //Used to build the childview
                TextView title = new TextView(getActivity());
                TextView desc = new TextView(getActivity());
                //set params for the title
                title.setText(res.getString(1));
                title.setTextSize(25);
                title.setPadding(10,0,10,0);
                title.setId(Integer.parseInt(res.getString(0)));
                //set the params for the description
                desc.setText(res.getString(2));
                desc.setTextSize(15);
                desc.setPadding(10,0,10,50);
                desc.setId(Integer.parseInt(res.getString(0)));
                desc.setClickable(true);
                desc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSelectedArticle(v);
                    }
                });
                //add the views
                childview.addView(title);
                childview.addView(desc);
            }
        }
        //add the child to their parents
        parentView.addView(childview);
    }

    /**
     * Method to change the view when an item is selected
     * The helper is used to find the selected item in the readersviefragment
     * @param v the actual view
     */
    private void getSelectedArticle(View v) {
        myDBH = new DatabaseHelper(getActivity());
        Cursor res = myDBH.getArticleDescription();
        FragmentManager fm2 = getFragmentManager();
        FragmentTransaction ft2 = fm2.beginTransaction();
        while(res.moveToNext()){
            if(v.getId() == Integer.parseInt(res.getString(0))){
                MainActivity.fh.setCenter("Article_reading_fragment");
                //Toast.makeText(getContext(),res.getString(1), Toast.LENGTH_SHORT).show();
                //This objects helps to know which article was clicked
                ArticleScreen.helper.setId(Integer.parseInt(res.getString(0)));
                //Then create the Fragment and activate it
                ReadersViewFragment readersview = new ReadersViewFragment();
                ft2.addToBackStack(null);
                ft2.hide(ArticleSelectionFragment.this);
                ft2.add(R.id.article_text_container,readersview,"Article_reading_fragment");
                ft2.commit();
            }
        }
    }
}
