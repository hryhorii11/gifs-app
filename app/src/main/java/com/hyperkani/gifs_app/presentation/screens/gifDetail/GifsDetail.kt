package com.hyperkani.gifs_app.presentation.screens.gifDetail

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
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
import com.hyperkani.gifs_app.presentation.screens.gisfList.GifsViewModel
import com.hyperkani.gifs_app.presentation.utils.UIState

@Composable
fun GifsDetailScreen(
    navHostController: NavHostController,
    viewModel: GifsViewModel
) {
    val gifs by viewModel.gifs.collectAsStateWithLifecycle()

    val selectedGifIndex by viewModel.selectedGifIndex.collectAsStateWithLifecycle()
    when (gifs) {
        is UIState.Error -> {
            GifLoadingErrorScreen(gifsState = gifs as UIState.Error<List<Gif>>) {

            }
        }

        is UIState.Idle -> {}
        is UIState.Loading -> {
            LoadingScreen()
        }

        is UIState.Success -> {
            GifsDetailScreenContent(
                gifs = (gifs as UIState.Success<List<Gif>>).data,
                selectedGifIndex = selectedGifIndex ?: 0
            ) { navHostController.navigateUp() }
        }
    }

}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun GifsDetailScreenContent(
    gifs: List<Gif>,
    selectedGifIndex: Int,
    back: () -> Unit
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

    val pagerState = rememberPagerState(initialPage = selectedGifIndex, pageCount = { gifs.size })

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            key = { gifs[it].url },
            pageSize = PageSize.Fill
        ) { index ->
            Column(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                AsyncImage(
                    model = gifs[index].images.original.url,
                    contentDescription = "Gif preview",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillWidth,
                    placeholder = painterResource(id = R.drawable.baseline_gif_box_24)
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.ic_back), contentDescription = stringResource(
                R.string.back_icon_desc
            ), modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .size(50.dp)
                .clickable {
                    back()
                }
        )
    }
}

@Preview
@Composable
fun GifsDetailScreenPreview() {
    GifsDetailScreenContent(
        listOf(
            Gif(
                "",
                "film",
                "fun",
                Images(OriginalImage(""))
            )
        ),
        0
    ) {}
}
