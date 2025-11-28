package com.plcoding.bookpedia.book.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailAction
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailScreenRoot
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailViewModel

import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import com.plcoding.bookpedia.book.presentation.book_list.SelectedBookViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        // Controller that manages the app navigation
        val navController = rememberNavController()
        // The NavHost is a container that displays the current screen based on the navigation state.
        NavHost(
            navController = navController,
            startDestination = Route.BookGraph
        ){
            // Group related screens under a parent route called BookGraph
            // This is important for the shared viewmodel
            navigation<Route.BookGraph>(
                startDestination = Route.BookList
            ){
                composable<Route.BookList> {
                    // Fetches a VM scoped to this screen. When you leave the screen, the VM is cleared
                    val viewModel = koinViewModel<BookListViewModel>()
                    // Fetches a VM scoped to the parent graph. This allows the data to persist even when
                    // navigating between screens. When you leave the graph, the VM is cleared
                    val selectedBookViewModel =
                        it.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    // Resets the selected book when navigating to this screen
                    LaunchedEffect(true){
                        selectedBookViewModel.onSelectBook(null)
                    }
                    // Loads the UI for the list of books
                    BookListScreenRoot(
                        viewModel = viewModel,
                        onBookClick = { book ->
                            selectedBookViewModel.onSelectBook(book)
                            navController.navigate(
                                Route.BookDetail(book.id)
                            )
                        }
                    )
                }
                composable<Route.BookDetail> {
                    // Because this screen is inside the same BookGraph, it retrieves the exact same
                    // instance of SelectedBookViewModel that the List screen used.
                    val selectedBookViewModel =
                        it.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    val viewModel = koinViewModel<BookDetailViewModel>()
                    val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()

                    LaunchedEffect(selectedBook){
                        selectedBook?.let {
                            viewModel.onAction(BookDetailAction.OnSelectedBookChange(it))
                        }
                    }

                    BookDetailScreenRoot(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}

/**
 * This extension of the NavBackStackEntry returns the shared viewmodel of the parent graph
 * This is used to share data between screens in the same graph
 */
@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    // Looks at the current screen's destination and retrieves the parent route
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    // Gets the BackStackEntry associated with that parent route
    val parentEntry = remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    // It tells Koin to use the parent's lifecycle rather than the current screen's one
    return koinViewModel(viewModelStoreOwner = parentEntry)
}