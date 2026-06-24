package com.madiwist.twitch.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.domain.models.Post
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.presentation.utils.Screen
import com.madiwist.twitch.utils.Constants

@Composable
fun Post(
    post: Post,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface),

    ) {
       Image(
           painter = painterResource(R.drawable.feed_image),
           contentDescription = "Post Image",
           modifier = Modifier.fillMaxWidth(),
           contentScale = ContentScale.FillWidth
       )
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .padding(SpaceMedium)
       ) {
           ActionRow(
               modifier = Modifier.fillMaxWidth(),
               username = "MADI",
               onLikeClick = { isLiked ->

               },
               onCommentClick = {

               },
               onShareClick = {

               },
               onUsernameClick = { username->

               }
           )
           Spacer(modifier = Modifier.height(SpaceSmall))
           Text(
               text = buildAnnotatedString {
                   append(post.description)
                   append(" ")
                   withStyle(
                       SpanStyle(
                           color = MaterialTheme.colorScheme.primary,
                           fontWeight = FontWeight.Bold,
                       )
                   ) {
                       append(stringResource(R.string.read_more))
                   }
               },
               color = MaterialTheme.colorScheme.onBackground,
               style = MaterialTheme.typography.bodyLarge,
               overflow = TextOverflow.Ellipsis,
               maxLines = Constants.MAX_POST_DESCRIPTION_LINES,
               modifier = Modifier.clickable{
                   navController.navigate(Screen.PostDetailsScreen.route)
               }
           )
           Spacer(modifier = Modifier.height(SpaceMedium))
           Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween
           ) {
               Text(
                   text = stringResource(R.string.liked_by_x_people, post.likeCount),
                   color = MaterialTheme.colorScheme.onPrimary,
                   style = MaterialTheme.typography.bodyMedium,
                   fontWeight = FontWeight.Bold
               )
               Text(
                   text = stringResource(R.string.x_comments, post.commentCount),
                   color = MaterialTheme.colorScheme.onPrimary,
                   style = MaterialTheme.typography.bodyMedium,
                   fontWeight = FontWeight.Bold
               )
           }
       }
    }
}

@Composable
fun EngagementButtons(
    modifier: Modifier = Modifier,
    onLikeClick : (Boolean) -> Unit = {},
    isLiked : Boolean = false,
    onCommentClick : () -> Unit = {},
    onShareClick : () -> Unit = {},
    iconSize: Dp = 30.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(
            onClick = {
                onLikeClick(!isLiked)
            },
            modifier = Modifier.size(iconSize)
        ) {
            Icon(
                imageVector = if (isLiked) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Filled.FavoriteBorder
                },
                contentDescription = if (isLiked) {
                    stringResource(R.string.unlike)
                } else {
                    stringResource(R.string.liked)
                },
                tint = if (isLiked) {
                    Color.Red
                } else {
                    MaterialTheme.colorScheme.onPrimary
                }
            )
        }
        Spacer(modifier = Modifier.width(SpaceMedium))
        IconButton(
            onClick = {
                onCommentClick()
            },
            modifier = Modifier.size(iconSize)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Comment,
                contentDescription = stringResource(R.string.comment)
            )
        }
        Spacer(modifier = Modifier.width(SpaceMedium))
        IconButton(
            onClick = {
                onShareClick()
            },
            modifier = Modifier.size(iconSize)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(R.string.share)
            )
        }
    }
}


@Composable
fun ActionRow(
    modifier: Modifier = Modifier,
    onLikeClick : (Boolean) -> Unit = {},
    isLiked : Boolean = false,
    onCommentClick : () -> Unit = {},
    onShareClick : () -> Unit = {},
    username : String,
    onUsernameClick : (String) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = username,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable{
                onUsernameClick(username)
            }
        )
        EngagementButtons(
            isLiked = isLiked,
            onLikeClick = onLikeClick,
            onCommentClick = onCommentClick,
            onShareClick = onShareClick
        )
    }
}













