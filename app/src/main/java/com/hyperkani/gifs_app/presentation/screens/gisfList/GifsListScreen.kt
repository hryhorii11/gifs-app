package com.hyperkani.gifs_app.presentation.screens.gisfList

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.hyperkani.gifs_app.R
import com.hyperkani.gifs_app.data.model.Gif
import com.hyperkani.gifs_app.data.model.Images
import com.hyperkani.gifs_app.data.model.OriginalImage
import com.hyperkani.gifs_app.presentation.components.GifLoadingErrorScreen
import com.hyperkani.gifs_app.presentation.components.LoadingScreen
import com.hyperkani.gifs_app.presentation.navigation.Screens
import com.hyperkani.gifs_app.presentation.ui.theme.Gifs_appTheme
import com.hyperkani.gifs_app.presentation.utils.UIState

@Composable
fun GifListScreen(
    navHostController: NavHostController,
    viewModel: GifsViewModel
) {

    val gifState by viewModel.gifs.collectAsState()

    GifListScreenContent(
        gifState = gifState,
        reload = { viewModel.setGifs() },
        search = { viewModel.search(it) },
        select = { viewModel.selectGif(it) },
        navHostController = navHostController
    )
}


@Composable
fun GifListScreenContent(
    gifState: UIState<List<Gif>>,
    search: (String) -> Unit,
    reload: () -> Unit,
    select: (Gif) -> Unit,
    navHostController: NavHostController? = null
) {
    val context = LocalContext.current
    val gifEnabledLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.gifs_list_screen_title),
            fontSize = dimensionResource(id = R.dimen.title_text_size).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.average_padding))
        )
        Button(
            onClick = {
                throw RuntimeException("Test Crash") // Force a crash
            }
        ) {
            Text("Crash")
        }
        when (gifState) {
            is UIState.Error -> {
                GifLoadingErrorScreen(gifState, reload)
            }

            is UIState.Idle -> {}
            is UIState.Loading -> {
                LoadingScreen()
            }

            is UIState.Success -> {
                ListScreen(gifState, gifEnabledLoader, search, select, navHostController)
            }
        }

    }
}

@Composable
fun ListScreen(
    videosState: UIState.Success<List<Gif>>,
    gifEnabledLoader: ImageLoader,
    search: (String) -> Unit,
    select: (Gif) -> Unit,
    navHostController: NavHostController?
) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

    TextField(
        value = searchQuery.value,
        onValueChange = {
            searchQuery.value = it
            search(it.text)
        },
        label = { Text("Search") },
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(12.dp))
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(videosState.data) { gif ->
            GifItem(gif, gifEnabledLoader) {
                navHostController?.navigate(Screens.GIF_SCREEN.route)
                select(gif)
            }
        }
    }
}


@Composable
fun GifItem(
    gif: Gif,
    gifEnabledLoader: ImageLoader,
    toDetails: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_padding))
            .clickable {
                toDetails()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.card_elevation))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.film_item_size))
                .padding(dimensionResource(id = R.dimen.small_padding))

        ) {

            AsyncImage(
                model = gif.images.original.url,
                contentDescription = "Video preview",
                imageLoader = gifEnabledLoader,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.film_icon_size))
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.baseline_gif_box_24)
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.average_padding)))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = gif.title,
                    fontSize = dimensionResource(id = R.dimen.medium_text_size).value.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = gif.username,
                    fontSize = dimensionResource(id = R.dimen.small_text_size).value.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun VideoListScreenPreview() {

    Gifs_appTheme {

        GifListScreenContent(
            gifState = UIState.Success(
                listOf(
                    Gif(
                        "",
                        "film",
                        "fun",
                        Images(OriginalImage(""))
                    )
                )
            ), {}, {}, {}
        )

    }
}