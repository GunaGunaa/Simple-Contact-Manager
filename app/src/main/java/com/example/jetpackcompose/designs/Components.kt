package com.example.jetpackcompose.designs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.jetpackcompose.R

class Components {
    @Composable
    fun Card(
        modifier: Modifier = Modifier.fillMaxWidth(),
        shape: CornerBasedShape = MaterialTheme.shapes.medium,
        backgroundColor: Color = MaterialTheme.colors.surface,
        contentColor: Color = contentColorFor(backgroundColor),
        border: BorderStroke? = null,
        elevation: Dp = 1.dp,
        content: @Composable () -> Unit
    ) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor,
            elevation = elevation,
            border = border,
            content = content
        )
        Spacer(modifier = Modifier.height(6.dp))
    }

    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier.padding(bottom = 4.dp),
        color: Color = colorResource(id = R.color.steel),
        fontSize: TextUnit = 14.sp,
        fontStyle: FontStyle? = null,
        fontWeight: FontWeight? = null,
        fontFamily: FontFamily? = null,
        letterSpacing: TextUnit = TextUnit.Unspecified,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        lineHeight: TextUnit = TextUnit.Unspecified,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        style: TextStyle = LocalTextStyle.current
    ) {

        val textColor = color.takeOrElse {
            style.color.takeOrElse {
                LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            }
        }
        // NOTE(text-perf-review): It might be worthwhile writing a bespoke merge implementation that
        // will avoid reallocating if all of the options here are the defaults
        val mergedStyle = style.merge(
            TextStyle(
                color = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight,
                textAlign = textAlign,
                lineHeight = lineHeight,
                fontFamily = fontFamily,
                textDecoration = textDecoration,
                fontStyle = fontStyle,
                letterSpacing = letterSpacing
            )
        )
        BasicText(
            text,
            modifier,
            mergedStyle,
            onTextLayout,
            overflow,
            softWrap,
            maxLines,
        )
    }

    @Composable
    fun SignupTextField(
        value: TextFieldValue,
        onValueChange: (TextFieldValue) -> Unit,
        modifier: Modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled: Boolean = true,
        readOnly: Boolean = false,
        textStyle: TextStyle = TextStyle(color = MaterialTheme.colors.primary),
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        isError: Boolean = false,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions(),
        singleLine: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        shape: Shape = TextFieldDefaults.TextFieldShape,
        colors: TextFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = colorResource(id = R.color.transparent),
            unfocusedIndicatorColor = colorResource(id = R.color.transparent),
            backgroundColor = colorResource(id = R.color.lightGrey)
        )
    ) {
        // If color is not provided via the text style, use content color as a default
        val textColor = textStyle.color.takeOrElse {
            colors.textColor(enabled).value
        }
        40.dp
        textStyle.merge(TextStyle(color = textColor))

        @OptIn(ExperimentalMaterialApi::class) (BasicTextField(value = value,
            modifier = modifier
                .background(colors.backgroundColor(enabled).value, shape)
                .indicatorLine(enabled, isError, interactionSource, colors)
                .defaultMinSize(
                    minWidth = TextFieldDefaults.MinWidth, minHeight = TextFieldDefaults.MinHeight
                ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = TextStyle(color = MaterialTheme.colors.primary),
            cursorBrush = SolidColor(colors.cursorColor(isError).value),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = true,
            maxLines = maxLines,
            decorationBox = @Composable { innerTextField ->
                // places leading icon, text field with label and placeholder, trailing icon
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value.text,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = placeholder,
                    label = label,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    singleLine = singleLine,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors
                )
            }))
    }

    @Composable
    fun AddressText(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = colorResource(id = R.color.steel),
        fontSize: TextUnit = 16.sp,
        fontStyle: FontStyle? = null,
        fontWeight: FontWeight? = null,
        fontFamily: FontFamily? = null,
        letterSpacing: TextUnit = TextUnit.Unspecified,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        lineHeight: TextUnit = TextUnit.Unspecified,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        style: TextStyle = LocalTextStyle.current
    ) {

        val textColor = color.takeOrElse {
            style.color.takeOrElse {
                LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            }
        }
        // NOTE(text-perf-review): It might be worthwhile writing a bespoke merge implementation that
        // will avoid reallocating if all of the options here are the defaults
        val mergedStyle = style.merge(
            TextStyle(
                color = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight,
                textAlign = textAlign,
                lineHeight = lineHeight,
                fontFamily = fontFamily,
                textDecoration = textDecoration,
                fontStyle = fontStyle,
                letterSpacing = letterSpacing
            )
        )
        BasicText(
            text,
            modifier,
            mergedStyle,
            onTextLayout,
            overflow,
            softWrap,
            maxLines,
        )
    }

    @Composable
    fun FullScreenProgressDialog(isOpen: Boolean) {
        if (isOpen) {
            Dialog(onDismissRequest = {}, content = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            })
        }
    }

    @Composable
    fun AlertDialog(message: String, onClick: () -> Unit) {
        val openDialog = remember {
            mutableStateOf(true)
        }
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text(text = "Alert", color = Color.Black) },
                text = { Text(message, color = Color.Black) },

                confirmButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                        onClick()
                    }) {
                        Text("ok", color = Color.Black)
                    }
                },
                contentColor = Color.Black
            )
        }
    }
    @Composable
    fun ErrorText(text: String) {
        Text(
            text = text,
            color = MaterialTheme.colors.error,
            fontSize = 12.sp
        )
    }

    @Composable
    fun SignupTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String = "",
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(
                    start = 0.dp
                ),
            textStyle = TextStyle(color = MaterialTheme.colors.primary),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id = R.color.transparent),
                unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                backgroundColor = colorResource(id = R.color.lightGrey),
            ),
            placeholder = { AddressText(text = placeholder) },
            )
    }
}

