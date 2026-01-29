package com.example.theshoppinglistapp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import kotlinx.serialization.internal.throwMissingFieldException
import kotlin.io.encoding.Base64

data class ShoppingItem(
    val Id: Int,
    var name: String,
    var quantity:Int,
    var isEditing:Boolean= false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }  // this will help us to store items in the list
    var showDialog by remember { mutableStateOf(false)}
    var itemName by remember{mutableStateOf("")}
    var itemQuantity by remember{mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize(),

    ){
        Button(
            onClick = { showDialog= true },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
        ) {
            Text(" LIST ")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ){
            items(sItems){
                item ->
                if(item.isEditing){
                    ShoppingItemEditor(item = item, onEditComplete ={
                        editedName, editedQuantity ->
                        sItems = sItems.map {it.copy(isEditing = false)}
                        val editedItem = sItems.find { it.Id == item.Id}
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                } else{
                    ShoppingListItem(item = item , onEditClick = {
                        // here we decide where to find out the item, and decide which one to change.
                        sItems =sItems.map{it.copy(isEditing = it.Id==item.Id)}
                    }, onDeleteClick = {
                        sItems =sItems-item
                    })
                }
            }
        }
    }

    if(showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick = {
                        if (itemName.isNotBlank()){
                            val newItem = ShoppingItem(
                                Id = sItems.size+1,
                                name = itemName,
                                quantity = itemQuantity.toInt()
                            )
                            sItems = sItems + newItem
                            showDialog = false
                            itemName = ""
                        }

                    }) {
                    Text("ADD")
                    }
                    Button(onClick = { showDialog=false }){
                     Text("CANCEL")
                    }
                }

            },
            title = { Text("Add Shopping Item") },
            text = {
                Column {

                    OutlinedTextField(        // this outline text field is used to create a box and take enteries in it
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )

                }
            }
        )
    }

    }

@Composable
fun ShoppingItemEditor(item: ShoppingItem , onEditComplete: (String ,Int) -> Unit){
    var editedName by remember {mutableStateOf(item.name)}
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember {mutableStateOf(item.isEditing)}

    Row(modifier = Modifier.fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly)
            {
            Column{
                BasicTextField(
                    value = editedName,
                    onValueChange = {editedName = it},
                    singleLine = true,
                    modifier = Modifier.wrapContentSize().padding(8.dp)
                )
                BasicTextField(
                    value = editedQuantity,
                    onValueChange = {editedQuantity = it},
                    singleLine = true,
                    modifier = Modifier.wrapContentSize().padding(8.dp)
                )
            }
                Button(
                    onClick = {
                        isEditing=false
                        onEditComplete(editedName,editedQuantity.toIntOrNull() ?: 1)
                    }
                )
                {
                    Text("SAVE ")
                }
            }
            }


@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: ()-> Unit,   //lambda expression
    onDeleteClick: ()-> Unit,
){
    Row(
        modifier = Modifier.padding(8.dp).fillMaxWidth().border(
            border = BorderStroke(4.dp, Color(0XFF018786)),
            shape = RoundedCornerShape(34)
        )
    ){
        Text(text = item.name ,modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}


//day 7 video -> created outline text field box,
// now we arrange how the list will look like. day 7.
// making the edit and delete symbol working.
