/*
 * Copyright (c) 2018 by Tran Le Duy
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

package com.duy.text.converter.core.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.duy.text.converter.R;
import com.duy.text.converter.core.activities.base.BaseActivity;
import com.duy.text.converter.core.fragments.BaseConverterFragment;

/**
 * Created by Duy on 2/17/2018.
 */

public class NumberConverterActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_converter);
        setupToolbar();
        setTitle(R.string.tab_title_base);

        BaseConverterFragment baseFragment
                = (BaseConverterFragment) getSupportFragmentManager().findFragmentByTag(BaseConverterFragment.class.getName());
        if (baseFragment == null) {
            baseFragment = BaseConverterFragment.newInstance();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, baseFragment, BaseConverterFragment.class.getName());
        fragmentTransaction.commit();
    }
}
