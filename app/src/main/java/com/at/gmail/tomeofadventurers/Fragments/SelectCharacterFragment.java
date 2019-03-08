package com.at.gmail.tomeofadventurers.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

public class SelectCharacterFragment extends Fragment implements View.OnClickListener {
    private static final String FILE_NAME = "example.txt";
    private Button button_save, button_load;
    EditText mEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_character, container, false);
        super.onCreate(savedInstanceState);
        mEditText = view.findViewById(R.id.edit_text);

        button_save = view.findViewById(R.id.button_save);

        button_save.setOnClickListener(this);

        button_load = view.findViewById(R.id.button_load);
        button_load.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                String text = mEditText.getText().toString();
                FileOutputStream fos = null;

                try {
                    fos = getActivity().openFileOutput(FILE_NAME, MODE_PRIVATE);
                    fos.write(text.getBytes());

                    mEditText.getText().clear();
                    Toast.makeText(getActivity().getApplicationContext(), "Saved to " +
                                    getActivity().getFilesDir().getAbsolutePath() + "/" + FILE_NAME,
                            Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.button_load:
                FileInputStream fis = null;

                try {
                    fis = getActivity().openFileInput(FILE_NAME);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String outPutText;

                    while ((outPutText = br.readLine()) != null) {
                        sb.append(outPutText).append("\n");
                    }

                    mEditText.setText(sb.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }

    }

}
