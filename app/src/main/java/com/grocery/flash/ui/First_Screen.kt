package com.grocery.flash.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grocery.flash.R

@Composable
fun firstScreen(
    flashViewModel1: FlashViewModel,
    onCategoryClicked: (Int) -> Unit
) {

    val context = LocalContext.current
    //use of below variable which will always check for changes in FlashUiState--
    //--by observing the public uiState present in ViewModel
    val flashUiState by flashViewModel1.uiState.collectAsState()
    //--and we can observe the value by calling FlashViewModel().uiState.CollectAsState()
    // the collecAsState() collects value from StateFlow(FlashViewModel) & represents it latest
    // -- value via State

    // now only thing that is pending that is use of flashUiState(abve decalared vraible) in our--
    // -- App's UI

    //CardCategory(context = context)
    //@Composable
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
//        item {
//            Text(text = flashUiState.clickStatus)
//        }
        item(
            span = { GridItemSpan(3) }
        ) {
            Column {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.thirtyperoff),
                    contentDescription = "30 % banner off",
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0, 200, 0, 2)
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Shop by category",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
        items(com.grocery.flash.Data.DataSource.loadCategories()) {
            CardCategory(
                context = context,
                it,
                flashViewModel = flashViewModel1,
                onCategoryClicked = onCategoryClicked
            )//,
        }
    }
}

@Composable
fun CardCategory(
    context: Context,
    categories: com.grocery.flash.Data.Categories,
    flashViewModel: FlashViewModel,
    onCategoryClicked: (Int) -> Unit
) {
    val itemTitle = stringResource(id = categories.stringResourceId)
    Card(colors = CardDefaults.cardColors(
        containerColor = Color(248, 221, 248, 255)
    ), modifier = Modifier.clickable {
        //flashViewModel.updateClickText(itemTitle)
        Toast.makeText(context, " clicked on ${itemTitle} ", Toast.LENGTH_SHORT).show()
        onCategoryClicked(categories.stringResourceId)
    }) {

        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = categories.stringResourceId),
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            ) // width and height
            Image(
                painter = painterResource(id = categories.imageResourceId),
                contentDescription = "iron man image",
                modifier = Modifier.size(110.dp)
            )

        }

    }
}