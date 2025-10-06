package com.example.daytask.presentation.shedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daytask.R
import com.example.daytask.data.local.model.Task
import com.example.daytask.domain.ProjectState
import com.example.daytask.presentation.home.components.CardImageRowItem
import com.example.daytask.presentation.home.components.OverlappingImagesRow
import com.example.daytask.presentation.ui.theme.bottomNavBarContainerColor
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Schedule(
    modifier: Modifier = Modifier,
    paddings: PaddingValues,
    projectState: ProjectState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateUp: () -> Unit
) {

    val currentDate = LocalDate.now()

    val days = getDatesBetween(currentDate, currentDate.plusDays(15))
    val dayInFormat = days.map { it.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")) }
    val (selected, onSelected) = remember { mutableStateOf(dayInFormat[0]) }

    val taskList = projectState.projects
        .flatMap { it.tasks }
        .sortedByDescending { it.dueDate }

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Surface(
                color = splashBgColor
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(bottom = 12.dp),
                    contentPadding = paddings
                ) {

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        ) {
                            IconButton(
                                onClick = navigateUp
                            ) {
                                Icon(
                                    tint = Color.White,
                                    contentDescription = null,
                                    painter = painterResource(R.drawable.arrowleft)
                                )
                            }
                            Text(
                                color = Color.White,
                                text = "Schedule"
                            )
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    tint = Color.White,
                                    contentDescription = null,
                                    painter = painterResource(R.drawable.addsquare)
                                )
                            }
                        }
                    }

                    item {

                        Spacer(Modifier.height(16.dp))
                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            fontSize = 20.sp,
                            text = LocalDate.now().month.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    item {

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            itemsIndexed(
                                key = { index, item -> "${index}${item}" },
                                items = dayInFormat
                            ) { index, day ->

                                TicketDatePickerItem(
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { (index + 1 * 10) * it },
                                            exit = slideOutHorizontally { (index + 1 * 10) * it }
                                        ),
                                    day = day,
                                    selected = selected,
                                    onSelected = onSelected,
                                    dayInNumber = days[index].format(DateTimeFormatter.ofPattern("d")),
                                    dayInText = days[index].format(DateTimeFormatter.ofPattern("E")),
                                )
                            }
                        }
                    }

                    item {

                        Spacer(Modifier.height(16.dp))
                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .padding(vertical = 8.dp, horizontal = 20.dp),
                            fontSize = 20.sp,
                            text = "Tasks",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    itemsIndexed(
                        key = { index, item -> "${index}${item.title}${item.id}" },
                        items = taskList
                            .filter { it.dueDate.contains(selected, ignoreCase = true) },
                    ) { index, item ->
                        TaskCardItem(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { (index + 1) * it },
                                    exit = slideOutHorizontally { -(index + 1) * -it }
                                )
                                .animateItem()
                                .padding(vertical = 8.dp, horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(80.dp),
                            item = item
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskCardItem(
    modifier: Modifier = Modifier,
    item: Task
) {
    Card(
        modifier = modifier,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = if (item.isComplete) yellowColor else bottomNavBarContainerColor,
            contentColor = if (item.isComplete) Color.Black else Color.White
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            if (!item.isComplete) {
                Box(
                    modifier = Modifier
                        .width(11.dp)
                        .fillMaxHeight()
                        .background(yellowColor)

                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            ) {
                Column {
                    Text(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        text = item.title
                    )
                    Text(
                        fontSize = 12.sp,
                        text = item.time
                    )
                }
                OverlappingImagesRow(
                    modifier = Modifier,
                    overlappingPercentage = 0.2f
                ) {
                    repeat(3) { CardImageRowItem() }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TicketDatePickerItem(
    modifier: Modifier = Modifier,
    day: String,
    dayInNumber: String,
    dayInText: String,
    selected: String,
    onSelected: (String) -> Unit
) {

    val datePickerColor =
        if (selected == day) yellowColor
        else bottomNavBarContainerColor

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
            .width(50.dp)
            .height(75.dp)
            .background(datePickerColor)
            .selectable(
                selected = (day == selected),
                onClick = { onSelected(day) }
            )
            .padding(8.dp)
    ) {
        Text(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected == day) Color.Black else Color.White,
            text = dayInNumber

        )
        Text(
            fontSize = 12.sp,
            color = if (selected == day) Color.Black else Color.White,
            text = dayInText
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDatesBetween(
    startDate: LocalDate,
    endDate: LocalDate
): List<LocalDate> {
    val numOfDays = ChronoUnit.DAYS.between(startDate, endDate)
    return Stream.iterate(startDate) { date ->
        date.plusDays(1)
    }
        .limit(numOfDays)
        .collect(Collectors.toList())
}