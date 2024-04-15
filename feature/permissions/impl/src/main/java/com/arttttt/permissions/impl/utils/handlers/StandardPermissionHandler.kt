package com.arttttt.permissions.impl.utils.handlers

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.arttttt.permissions.api.Permission2
import com.arttttt.permissions.impl.domain.entity.StandardPermission
import com.arttttt.permissions.impl.utils.PermissionHandler
import com.arttttt.permissions.impl.utils.of
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

internal class StandardPermissionHandler : PermissionHandler<StandardPermission> {

    private val resultFlow = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)

    override suspend fun requestPermission(
        activity: ComponentActivity,
        permission: StandardPermission
    ): Permission2.Status {
        val key = "permission_request_${permission.permission}"

        val callback = ActivityResultCallback<Boolean> { result ->
            resultFlow.tryEmit(result)
        }

        val launcher = activity.activityResultRegistry.register(key, ActivityResultContracts.RequestPermission(), callback)

        return try {
            launcher.launch(permission.permission)
            Permission2.Status.of(resultFlow.first())
        } finally {
            launcher.unregister()
        }
    }
}
