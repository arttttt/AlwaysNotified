package com.arttttt.appholder.utils

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.domain.entity.permission.StandardPermission
import com.arttttt.appholder.of
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
