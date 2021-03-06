/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.text.converter.core.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.duy.text.converter.R;
import com.duy.text.converter.core.codec.interfaces.CodecMethod;
import com.duy.text.converter.core.utils.ClipboardUtil;
import com.duy.text.converter.core.utils.ShareManager;
import com.duy.text.converter.core.view.BaseEditText;
import com.duy.text.converter.core.view.RoundedBackgroundEditText;
import com.duy.text.converter.pro.activities.CodecAllActivity;
import com.duy.text.converter.pro.license.Premium;


/**
 * TextFragment
 * Created by DUy on 06-Feb-17.
 */

public class CodecFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "CodecFragment";
    private BaseEditText mInput, mOutput;
    private Spinner mMethodSpinner;
    private TextWatcher mOutputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mOutput.isFocused()) convert(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher mInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mInput.isFocused()) convert(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static CodecFragment newInstance(String init) {
        CodecFragment fragment = new CodecFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Intent.EXTRA_TEXT, init);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_codec, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInput = view.findViewById(R.id.edit_input);
        mInput.setBackgroundDrawable(RoundedBackgroundEditText.createRoundedBackground(getContext()));

        mOutput = view.findViewById(R.id.edit_output);
        mOutput.setBackgroundDrawable(RoundedBackgroundEditText.createRoundedBackground(getContext()));

        mInput.addTextChangedListener(mInputWatcher);
        mOutput.addTextChangedListener(mOutputWatcher);

        view.findViewById(R.id.image_paste).setOnClickListener(this);
        view.findViewById(R.id.image_paste_out).setOnClickListener(this);
        view.findViewById(R.id.img_copy).setOnClickListener(this);
        view.findViewById(R.id.img_copy_out).setOnClickListener(this);
        view.findViewById(R.id.btn_share).setOnClickListener(this);
        view.findViewById(R.id.img_share_out).setOnClickListener(this);
        view.findViewById(R.id.img_encode_all).setOnClickListener(this);
        view.findViewById(R.id.img_decode_all).setOnClickListener(this);

        CodecMethod[] codecMethods = CodecMethod.values();
        String[] names = new String[codecMethods.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = codecMethods[i].getCodec().getName(getContext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, names);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mMethodSpinner = view.findViewById(R.id.spinner_codec_methods);
        mMethodSpinner.setBackgroundDrawable(RoundedBackgroundEditText.createRoundedBackground(getContext()));
        mMethodSpinner.setAdapter(adapter);
        mMethodSpinner.setOnItemSelectedListener(this);

        restore();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.img_copy:
                ClipboardUtil.setClipboard(getContext(), mInput.getText().toString());

                break;
            case R.id.image_paste:
                mInput.setText(ClipboardUtil.getClipboard(getContext()));

                break;
            case R.id.img_copy_out:
                ClipboardUtil.setClipboard(getContext(), mOutput.getText().toString());

                break;
            case R.id.image_paste_out:
                mOutput.setText(ClipboardUtil.getClipboard(getContext()));

                break;
            case R.id.btn_share:
                shareText(mInput);

                break;
            case R.id.img_share_out:
                shareText(mOutput);

                break;
            case R.id.img_encode_all:
                encodeAll();
                break;
            case R.id.img_decode_all:
                decodeAll();
                break;
        }
    }

    private void decodeAll() {
        if (!Premium.isPremium(getContext())) {
            showDialogUpgrade();
        } else {
            Intent intent = new Intent(getContext(), CodecAllActivity.class);
            intent.setAction(CodecAllActivity.EXTRA_ACTION_DECODE);
            intent.putExtra(CodecAllActivity.EXTRA_INPUT, mOutput.getText().toString());
            startActivity(intent);
        }
    }

    private void encodeAll() {
        if (!Premium.isPremium(getContext())) {
            showDialogUpgrade();
        } else {
            Intent intent = new Intent(getContext(), CodecAllActivity.class);
            intent.setAction(CodecAllActivity.EXTRA_ACTION_ENCODE);
            intent.putExtra(CodecAllActivity.EXTRA_INPUT, mInput.getText().toString());
            startActivity(intent);
        }
    }

    private void showDialogUpgrade() {
        Premium.upgrade(getActivity());
    }

    @Override
    public void onDestroyView() {
        saveData();
        mInput.removeTextChangedListener(mInputWatcher);
        mOutput.removeTextChangedListener(mOutputWatcher);
        super.onDestroyView();
    }

    private void shareText(EditText editText) {
        ShareManager.share(editText.getText().toString(), getContext());
    }

    private void convert(boolean isEncode) {
        int index = mMethodSpinner.getSelectedItemPosition();
        CodecMethod method = CodecMethod.values()[index];
        if (isEncode) {
            String inp = mInput.getText().toString();
            mOutput.setText(method.getCodec().encode(inp));
        } else {
            String out = mOutput.getText().toString();
            mInput.setText(method.getCodec().decode(out));
        }
    }


    public void saveData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        pref.edit().putString(TAG, mInput.getText().toString()).apply();
    }

    public void restore() {
        String text = getArguments().getString(Intent.EXTRA_TEXT, "");
        if (!text.isEmpty()) {
            mInput.setText(text);
        } else {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            mInput.setText(pref.getString(TAG, ""));
        }
        convert(true);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (mInput.hasFocus()) {
            convert(true);
        } else {
            convert(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
