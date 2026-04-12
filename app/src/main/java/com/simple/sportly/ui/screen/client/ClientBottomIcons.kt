package com.simple.sportly.ui.screen.client

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal object ClientBottomIcons {
    val Activities: ImageVector
        get() = ImageVector.Builder(
            name = "ClientActivities",
            defaultWidth = 18.dp,
            defaultHeight = 18.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(2f, 18f)
                curveTo(1.45f, 18f, 0.979167f, 17.8042f, 0.5875f, 17.4125f)
                curveTo(0.195833f, 17.0208f, 0f, 16.55f, 0f, 16f)
                verticalLineTo(2f)
                curveTo(0f, 1.45f, 0.195833f, 0.979167f, 0.5875f, 0.5875f)
                curveTo(0.979167f, 0.195833f, 1.45f, 0f, 2f, 0f)
                horizontalLineTo(16f)
                curveTo(16.55f, 0f, 17.0208f, 0.195833f, 17.4125f, 0.5875f)
                curveTo(17.8042f, 0.979167f, 18f, 1.45f, 18f, 2f)
                verticalLineTo(16f)
                curveTo(18f, 16.55f, 17.8042f, 17.0208f, 17.4125f, 17.4125f)
                curveTo(17.0208f, 17.8042f, 16.55f, 18f, 16f, 18f)
                horizontalLineTo(2f)
                close()
                moveTo(2f, 16f)
                horizontalLineTo(16f)
                verticalLineTo(5f)
                horizontalLineTo(2f)
                verticalLineTo(16f)
                close()
            }
        }.build()

    val Notifications: ImageVector
        get() = ImageVector.Builder(
            name = "ClientNotifications",
            defaultWidth = 16.dp,
            defaultHeight = 20.dp,
            viewportWidth = 16f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(0f, 17f)
                verticalLineTo(15f)
                horizontalLineTo(2f)
                verticalLineTo(8f)
                curveTo(2f, 6.61667f, 2.41667f, 5.3875f, 3.25f, 4.3125f)
                curveTo(4.08333f, 3.2375f, 5.16667f, 2.53333f, 6.5f, 2.2f)
                verticalLineTo(1.5f)
                curveTo(6.5f, 1.08333f, 6.64583f, 0.729167f, 6.9375f, 0.4375f)
                curveTo(7.22917f, 0.145833f, 7.58333f, 0f, 8f, 0f)
                curveTo(8.41667f, 0f, 8.77083f, 0.145833f, 9.0625f, 0.4375f)
                curveTo(9.35417f, 0.729167f, 9.5f, 1.08333f, 9.5f, 1.5f)
                verticalLineTo(2.2f)
                curveTo(10.8333f, 2.53333f, 11.9167f, 3.2375f, 12.75f, 4.3125f)
                curveTo(13.5833f, 5.3875f, 14f, 6.61667f, 14f, 8f)
                verticalLineTo(15f)
                horizontalLineTo(16f)
                verticalLineTo(17f)
                horizontalLineTo(0f)
                close()
                moveTo(8f, 20f)
                curveTo(7.45f, 20f, 6.97917f, 19.8042f, 6.5875f, 19.4125f)
                curveTo(6.19583f, 19.0208f, 6f, 18.55f, 6f, 18f)
                horizontalLineTo(10f)
                curveTo(10f, 18.55f, 9.80417f, 19.0208f, 9.4125f, 19.4125f)
                curveTo(9.02083f, 19.8042f, 8.55f, 20f, 8f, 20f)
                close()
                moveTo(4f, 15f)
                horizontalLineTo(12f)
                verticalLineTo(8f)
                curveTo(12f, 6.9f, 11.6083f, 5.95833f, 10.825f, 5.175f)
                curveTo(10.0417f, 4.39167f, 9.1f, 4f, 8f, 4f)
                curveTo(6.9f, 4f, 5.95833f, 4.39167f, 5.175f, 5.175f)
                curveTo(4.39167f, 5.95833f, 4f, 6.9f, 4f, 8f)
                verticalLineTo(15f)
                close()
            }
        }.build()

    val Refresh: ImageVector
        get() = ImageVector.Builder(
            name = "ClientRefresh",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(9f, 19.9f)
                curveTo(6.43333f, 19.65f, 4.29167f, 18.575f, 2.575f, 16.675f)
                curveTo(0.858333f, 14.775f, 0f, 12.5333f, 0f, 9.95f)
                curveTo(0f, 7.36667f, 0.858333f, 5.125f, 2.575f, 3.225f)
                curveTo(4.29167f, 1.325f, 6.43333f, 0.25f, 9f, 0f)
                verticalLineTo(3f)
                curveTo(7.26667f, 3.23333f, 5.83333f, 4.00833f, 4.7f, 5.325f)
                curveTo(3.56667f, 6.64167f, 3f, 8.18333f, 3f, 9.95f)
                curveTo(3f, 11.7167f, 3.56667f, 13.2583f, 4.7f, 14.575f)
                curveTo(5.83333f, 15.8917f, 7.26667f, 16.6667f, 9f, 16.9f)
                verticalLineTo(19.9f)
                close()
                moveTo(11f, 19.9f)
                verticalLineTo(16.9f)
                curveTo(12.5667f, 16.7f, 13.8917f, 16.05f, 14.975f, 14.95f)
                curveTo(16.0583f, 13.85f, 16.7167f, 12.5167f, 16.95f, 10.95f)
                horizontalLineTo(19.95f)
                curveTo(19.7167f, 13.3333f, 18.7625f, 15.3625f, 17.0875f, 17.0375f)
                curveTo(15.4125f, 18.7125f, 13.3833f, 19.6667f, 11f, 19.9f)
                close()
                moveTo(16.95f, 8.95f)
                curveTo(16.7167f, 7.38333f, 16.0583f, 6.05f, 14.975f, 4.95f)
                curveTo(13.8917f, 3.85f, 12.5667f, 3.2f, 11f, 3f)
                verticalLineTo(0f)
                curveTo(13.3833f, 0.233333f, 15.4125f, 1.1875f, 17.0875f, 2.8625f)
                curveTo(18.7625f, 4.5375f, 19.7167f, 6.56667f, 19.95f, 8.95f)
                horizontalLineTo(16.95f)
                close()
            }
        }.build()

    val Profile: ImageVector
        get() = ImageVector.Builder(
            name = "ClientProfile",
            defaultWidth = 21.dp,
            defaultHeight = 21.dp,
            viewportWidth = 21f,
            viewportHeight = 21f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(4.01042f, 15.7292f)
                curveTo(4.89583f, 15.0521f, 5.88542f, 14.5182f, 6.97917f, 14.1276f)
                curveTo(8.07292f, 13.737f, 9.21875f, 13.5417f, 10.4167f, 13.5417f)
                curveTo(11.6146f, 13.5417f, 12.7604f, 13.737f, 13.8542f, 14.1276f)
                curveTo(14.9479f, 14.5182f, 15.9375f, 15.0521f, 16.8229f, 15.7292f)
                curveTo(17.4306f, 15.0174f, 17.9036f, 14.2101f, 18.2422f, 13.3073f)
                curveTo(18.5807f, 12.4045f, 18.75f, 11.441f, 18.75f, 10.4167f)
                curveTo(18.75f, 8.10764f, 17.9384f, 6.14149f, 16.3151f, 4.51823f)
                curveTo(14.6918f, 2.89497f, 12.7257f, 2.08333f, 10.4167f, 2.08333f)
                curveTo(8.10764f, 2.08333f, 6.14149f, 2.89497f, 4.51823f, 4.51823f)
                curveTo(2.89497f, 6.14149f, 2.08333f, 8.10764f, 2.08333f, 10.4167f)
                curveTo(2.08333f, 11.441f, 2.2526f, 12.4045f, 2.59115f, 13.3073f)
                curveTo(2.92969f, 14.2101f, 3.40278f, 15.0174f, 4.01042f, 15.7292f)
                close()
                moveTo(10.4167f, 11.4583f)
                curveTo(9.39236f, 11.4583f, 8.52865f, 11.1068f, 7.82552f, 10.4036f)
                curveTo(7.1224f, 9.70052f, 6.77083f, 8.83681f, 6.77083f, 7.8125f)
                curveTo(6.77083f, 6.7882f, 7.1224f, 5.92448f, 7.82552f, 5.22135f)
                curveTo(8.52865f, 4.51823f, 9.39236f, 4.16667f, 10.4167f, 4.16667f)
                curveTo(11.441f, 4.16667f, 12.3047f, 4.51823f, 13.0078f, 5.22135f)
                curveTo(13.7109f, 5.92448f, 14.0625f, 6.7882f, 14.0625f, 7.8125f)
                curveTo(14.0625f, 8.83681f, 13.7109f, 9.70052f, 13.0078f, 10.4036f)
                curveTo(12.3047f, 11.1068f, 11.441f, 11.4583f, 10.4167f, 11.4583f)
                close()
                moveTo(10.4167f, 20.8333f)
                curveTo(8.9757f, 20.8333f, 7.62153f, 20.5599f, 6.35417f, 20.013f)
                curveTo(5.08681f, 19.4661f, 3.98438f, 18.724f, 3.04688f, 17.7865f)
                curveTo(2.10938f, 16.849f, 1.36719f, 15.7465f, 0.820313f, 14.4792f)
                curveTo(0.273438f, 13.2118f, 0f, 11.8576f, 0f, 10.4167f)
                curveTo(0f, 8.9757f, 0.273438f, 7.62153f, 0.820313f, 6.35417f)
                curveTo(1.36719f, 5.08681f, 2.10938f, 3.98438f, 3.04688f, 3.04688f)
                curveTo(3.98438f, 2.10938f, 5.08681f, 1.36719f, 6.35417f, 0.820313f)
                curveTo(7.62153f, 0.273438f, 8.9757f, 0f, 10.4167f, 0f)
                curveTo(11.8576f, 0f, 13.2118f, 0.273438f, 14.4792f, 0.820313f)
                curveTo(15.7465f, 1.36719f, 16.849f, 2.10938f, 17.7865f, 3.04688f)
                curveTo(18.724f, 3.98438f, 19.4661f, 5.08681f, 20.013f, 6.35417f)
                curveTo(20.5599f, 7.62153f, 20.8333f, 8.9757f, 20.8333f, 10.4167f)
                curveTo(20.8333f, 11.8576f, 20.5599f, 13.2118f, 20.013f, 14.4792f)
                curveTo(19.4661f, 15.7465f, 18.724f, 16.849f, 17.7865f, 17.7865f)
                curveTo(16.849f, 18.724f, 15.7465f, 19.4661f, 14.4792f, 20.013f)
                curveTo(13.2118f, 20.5599f, 11.8576f, 20.8333f, 10.4167f, 20.8333f)
                close()
                moveTo(10.4167f, 18.75f)
                curveTo(11.3368f, 18.75f, 12.2049f, 18.6155f, 13.0208f, 18.3464f)
                curveTo(13.8368f, 18.0773f, 14.5833f, 17.691f, 15.2604f, 17.1875f)
                curveTo(14.5833f, 16.684f, 13.8368f, 16.2977f, 13.0208f, 16.0286f)
                curveTo(12.2049f, 15.7596f, 11.3368f, 15.625f, 10.4167f, 15.625f)
                curveTo(9.49653f, 15.625f, 8.62847f, 15.7596f, 7.8125f, 16.0286f)
                curveTo(6.99653f, 16.2977f, 6.25f, 16.684f, 5.57292f, 17.1875f)
                curveTo(6.25f, 17.691f, 6.99653f, 18.0773f, 7.8125f, 18.3464f)
                curveTo(8.62847f, 18.6155f, 9.49653f, 18.75f, 10.4167f, 18.75f)
                close()
                moveTo(10.4167f, 9.375f)
                curveTo(10.8681f, 9.375f, 11.2413f, 9.22743f, 11.5365f, 8.93229f)
                curveTo(11.8316f, 8.63715f, 11.9792f, 8.26389f, 11.9792f, 7.8125f)
                curveTo(11.9792f, 7.36111f, 11.8316f, 6.98785f, 11.5365f, 6.69271f)
                curveTo(11.2413f, 6.39757f, 10.8681f, 6.25f, 10.4167f, 6.25f)
                curveTo(9.96528f, 6.25f, 9.59202f, 6.39757f, 9.29688f, 6.69271f)
                curveTo(9.00174f, 6.98785f, 8.85417f, 7.36111f, 8.85417f, 7.8125f)
                curveTo(8.85417f, 8.26389f, 9.00174f, 8.63715f, 9.29688f, 8.93229f)
                curveTo(9.59202f, 9.22743f, 9.96528f, 9.375f, 10.4167f, 9.375f)
                close()
            }
        }.build()
}


