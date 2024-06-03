package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.room.Room
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.littlelemon.ui.theme.highlightColor1
import com.example.littlelemon.ui.theme.highlightColor2
import com.example.littlelemon.ui.theme.karla
import com.example.littlelemon.ui.theme.markazi
import com.example.littlelemon.ui.theme.primaryGreen
import com.example.littlelemon.ui.theme.primaryYellow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.NumberFormat

@Composable
fun Home(navController: NavHostController, context: Context) {
    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database"
    ).build()
    val menuItems by db.menuItemDao().getAll().observeAsState(emptyList())
    val searchPhrase = remember { mutableStateOf("") }
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    suspend fun insertMenuToDatabase(menuItemsNetwork: List<MenuItemNetwork>) {
        val menuItemsRoom = menuItemsNetwork.map { it.toMenuItemRoom() }
        coroutineScope {
            async(Dispatchers.IO) {
                if (db.menuItemDao().isEmpty()) {
                    db.menuItemDao().insertAll(*menuItemsRoom.toTypedArray())
                }
            }
        }
    }

    suspend fun fetchMenu(): List<MenuItemNetwork> {
        return client
            .get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
            .body<MenuNetwork>()
            .menu
    }

    LaunchedEffect(Unit) {
        insertMenuToDatabase(fetchMenu())
    }

    Column() {
            Header(navController)
            Hero(){ searchPhrase.value = it}
            FoodMenuList(menuItems, searchPhrase)
        }
    }

@Composable
fun Header(navController: NavHostController){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Spacer(modifier = Modifier.width(50.dp))
        Image(painter = painterResource(id = R.drawable.logo),
            contentDescription = "Little Lemon Logo",
            modifier = Modifier
                .fillMaxWidth(0.7f))
        Box(modifier = Modifier
            .size(50.dp)
            .clickable { navController.navigate(Profile.route) }){
            Image(painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile Icon",
                modifier = Modifier
                    .fillMaxSize())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Hero(search: (input: String) -> Unit) {
    val searchPhrase = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(primaryGreen)
                .padding(horizontal = 20.dp, vertical = 0.dp)
        ) {
            Text(
                text = "Little Lemon",
                fontFamily = markazi,
                fontWeight = FontWeight.Medium,
                fontSize = 54.sp,
                color = primaryYellow,
                modifier = Modifier.fillMaxWidth(1f).size(50.dp)
            )
            Text(text = "Chicago",
                color = Color.White,
                fontFamily = markazi,
                fontWeight = FontWeight.Medium,
                fontSize = 28.sp,
                modifier = Modifier.fillMaxWidth(1f).size(28.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(140.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.",
                    modifier = Modifier.fillMaxWidth(0.6f),
                    color = Color.White,
                    fontFamily = karla,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.hero_image),
                    contentDescription = "Hero Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            OutlinedTextField(
                value = searchPhrase.value,
                onValueChange = {
                    searchPhrase.value = it
                    search(searchPhrase.value)
                },
                placeholder = {
                    Text(text = "Enter search phrase")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun FoodMenuList(databaseMenuItems: List<MenuItemRoom>, search: MutableState<String>) {
    val categories = databaseMenuItems.map {
        it.category.replaceFirstChar {character ->
            character.uppercase()
        }
    }.toSet()

    val selectedCategory = remember {
        mutableStateOf("")
    }

    val items = if(search.value == ""){
        databaseMenuItems
    } else {
        databaseMenuItems.filter {
            it.title.contains(search.value, ignoreCase = true)
        }
    }

    val filteredItems = if(selectedCategory.value == "" || selectedCategory.value == "all"){
        items } else {
        items.filter {
            it.category.contains(selectedCategory.value, ignoreCase = true)
        }
    }

    Column{
        MenuCategories(categories) { selectedCategory.value = it }
        Spacer(modifier = Modifier.size(2.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            for (item in filteredItems) {
                MenuItem(item = item)
            }
        }
    }
}

@Composable
fun MenuCategories(categories: Set<String>, categoryLambda : (sel: String) -> Unit) {
    val selection = remember { mutableStateOf("") }
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = highlightColor2,
            disabledContainerColor = Color.White,
            disabledContentColor = highlightColor2),
        modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            Text(text = "ORDER FOR DELIVERY!", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CategoryButton(category = "All"){
                    selection.value = it.lowercase()
                    categoryLambda(it.lowercase())
                }
                for (category in categories){
                    CategoryButton(category = category){
                        selection.value = it
                        categoryLambda(it)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryButton(category:String, selectedCategory: (sel: String) -> Unit) {
    val isClicked = remember{
        mutableStateOf(false)
    }
    Button(onClick = {
        isClicked.value = !isClicked.value
        selectedCategory(category)
    },
        colors = ButtonDefaults.buttonColors(
            containerColor = highlightColor1,
            contentColor = primaryGreen
        )) {
        Text(text = category)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(item: MenuItemRoom) {
    val itemDescription = if(item.description.length>100) {
        item.description.substring(0,100) + "..."
    }
    else{
        item.description
    }
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = highlightColor2,
            disabledContainerColor = Color.White,
            disabledContentColor = highlightColor2)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.fillMaxWidth(0.7f),
                verticalArrangement = Arrangement.SpaceBetween) {
                Text(text = item.title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
                Text(text = itemDescription, modifier = Modifier.padding(bottom = 10.dp))
                Text(NumberFormat.getCurrencyInstance().format(item.price), fontWeight = FontWeight.Bold)
            }
            GlideImage(model = item.image,
                contentDescription = "",
                Modifier.size(100.dp, 100.dp),
                contentScale = ContentScale.Crop)
        }
    }

}