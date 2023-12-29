package com.example.jetmusic.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.rounded.PlayCircleFilled
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetmusic.R
import com.example.jetmusic.data.entities.Song
import com.example.jetmusic.other.Resource
import com.example.jetmusic.ui.viewmodels.MainViewModel
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun HomeScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {

    Surface(modifier = Modifier.fillMaxSize()) {
        HomeContent(
            modifier = Modifier.fillMaxSize(),
            music = viewModel.mediaItems.value,
            viewModel = viewModel
        )
    }
}


@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    music: Resource<List<Song>>,
    viewModel: MainViewModel,
) {
    val currentPlayingSong by remember {
        viewModel.currentPlayingSong
    }

    val playbackState by viewModel.playbackState.observeAsState()

    Column(modifier = modifier) {
        val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)
        Spacer(
            Modifier
                .background(appBarColor)
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
        )
        HomeAppBar(
            backgroundColor = appBarColor,
            modifier = Modifier.fillMaxWidth()
        )
        when (music) {
            is Resource.Success -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                )
                {
                    Column {
                        MusicListRow(viewModel = viewModel, songs = music.data!!)
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.background)
                                .padding(bottom = 60.dp)
                        ) {
                            items(music.data!!) {
                                MusicListItem(
                                    viewModel = viewModel,
                                    song = it
                                )
                            }
                        }
                    }
                }
            }

            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .size(100.dp)
                            .fillMaxHeight()

                            .align(Alignment.Center)
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                    )
                }

            }

            is Resource.Error -> TODO()
        }
    }
}

@Composable
fun MusicListRow(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    songs: List<Song>,

    ) {
    val lastIndex = songs.size - 1
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = 24.dp, top = 8.dp, end = 24.dp, bottom = 24.dp)
    ) {
        itemsIndexed(
            items = songs
        ) { index, item ->
            MusicListRowItem(
                podcastTitle = item.title,
                podcastImageUrl = item.imageUrl,
                onToggleFollowClicked = {
                    viewModel.playOrToggleSong(item)
                    viewModel.showPlayerFullScreen = true
                },
                modifier = Modifier.width(128.dp)
            )

            if (index < lastIndex) Spacer(Modifier.width(24.dp))
        }
    }
}

@Composable
fun MusicListRowItem(
    podcastTitle: String,
    podcastImageUrl: String,
    onToggleFollowClicked: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier.semantics(mergeDescendants = true) {}
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = rememberCoilPainter(podcastImageUrl, fadeIn = true),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
            )

            IconButton(
                onClick = onToggleFollowClicked,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Default.PlayCircleFilled,
                    contentDescription = stringResource(R.string.cd_play)
                )
            }
        }

        Text(
            text = podcastTitle,
            style = MaterialTheme.typography.body1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun MusicListItem(
    viewModel: MainViewModel,
    song: Song,
) {
    ConstraintLayout(
        modifier = Modifier
            .clickable {
                viewModel.playOrToggleSong(song)
                viewModel.showPlayerFullScreen = true
            }
            .fillMaxWidth()
    ) {
        val (
            divider, songTitle, songSubtitle, image, playIcon, overflow, addPlaylist,
        ) = createRefs()

        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)

                width = Dimension.fillToConstraints
            }
        )

        Image(
            painter = rememberCoilPainter(song.imageUrl, fadeIn = true),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(image) {
                    end.linkTo(parent.end, 16.dp)
                    top.linkTo(parent.top, 16.dp)
                }
        )

        Text(
            text = song.title,
            maxLines = 2,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(songTitle) {
                linkTo(
                    start = parent.start,
                    end = image.start,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 16.dp)
                width = Dimension.preferredWrapContent
            }
        )

        val titleImageBarrier = createBottomBarrier(divider, image)

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = song.artist,
                maxLines = 2,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.constrainAs(songSubtitle) {
                    linkTo(
                        start = parent.start,
                        end = image.start,
                        startMargin = 24.dp,
                        bias = 0f
                    )
                    top.linkTo(songTitle.bottom, 6.dp)
                    start.linkTo(parent.start, 16.dp)
                    height = Dimension.preferredWrapContent
                    width = Dimension.preferredWrapContent
                }
            )
        }

        Image(
            imageVector = Icons.Rounded.PlayCircleFilled,
            contentDescription = stringResource(R.string.cd_play),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(LocalContentColor.current),
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false, radius = 24.dp)
                ) { /* TODO */ }
                .size(48.dp)
                .padding(6.dp)
                .semantics { role = Role.Button }
                .constrainAs(playIcon) {
                    start.linkTo(parent.start, 24.dp)
                    top.linkTo(songSubtitle.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                }
        )

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier.constrainAs(addPlaylist) {
                    end.linkTo(overflow.start)
                    centerVerticallyTo(playIcon)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PlaylistAdd,
                    contentDescription = stringResource(R.string.cd_play)
                )
            }

            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier.constrainAs(overflow) {
                    end.linkTo(parent.end, 8.dp)
                    centerVerticallyTo(playIcon)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.cd_play)
                )
            }
        }
    }
}