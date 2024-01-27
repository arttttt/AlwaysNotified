package com.arttttt.alwaysnotified.utils.permissions.handlers

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.arttttt.alwaysnotified.domain.entity.permission.Permission2
import com.arttttt.alwaysnotified.domain.entity.permission.StandardPermission
import com.arttttt.alwaysnotified.utils.extensions.of
import com.arttttt.alwaysnotified.utils.permissions.PermissionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class StandardPermissionHandler : PermissionHandler<StandardPermission> {

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
