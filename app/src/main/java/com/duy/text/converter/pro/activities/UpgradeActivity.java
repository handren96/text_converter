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

package com.duy.text.converter.pro.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.duy.common.activities.ActivityHelper;
import com.duy.common.purchase.InAppPurchaseActivity;
import com.duy.text.converter.R;
import com.duy.text.converter.core.adapters.PremiumFeatureAdapter;

/**
 * Created by Duy on 25-Dec-17.
 */

public class UpgradeActivity extends InAppPurchaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        ActivityHelper.setupToolbar(this);
        setTitle(getString(R.string.pro_version));
        findViewById(R.id.btn_upgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpgrade();
            }
        });
        setResult(RESULT_CANCELED);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PremiumFeatureAdapter(this));

    }

    @Override
    public void updateUi(boolean premium) {
        super.updateUi(premium);
        if (premium) {
            setResult(RESULT_OK);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upgrade, menu);
        ;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_upgrade) {
            clickUpgrade();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
