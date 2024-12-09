package com.hyperkani.gifs_app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.hyperkani.gifs_app.R
import com.hyperkani.gifs_app.data.model.Gif
import com.hyperkani.gifs_app.data.model.GifError
import com.hyperkani.gifs_app.presentation.utils.UIState

@Composable
fun GifLoadingErrorScreen(gifsState: UIState.Error<List<Gif>>, reload: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.average_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = stringResource(R.string.error_icon),
            modifier = Modifier.size(dimensionResource(id = R.dimen.error_icon_size)),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.average_padding)))

        Text(
            text = when (gifsState.error) {
                is GifError.NetworkError -> stringResource(R.string.internet_exception_message)
                is GifError.ServerError -> stringResource(R.string.server_error_message)
                is GifError.ApiError -> stringResource(R.string.api_error) + gifsState.error.message
                is GifError.UnknownError -> stringResource(R.string.unknown_error_message)
                else -> stringResource(R.string.other_error_message)
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.big_spacer_height)))

        Button(
            onClick = { reload() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(stringResource(R.string.try_again_button_text))
        }
    }
}