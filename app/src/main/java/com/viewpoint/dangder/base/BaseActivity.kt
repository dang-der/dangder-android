package com.viewpoint.dangder.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.view.dialog.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable


abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: B
    protected abstract val layoutId: Int
    protected lateinit var loadingDialog: LoadingDialog
    protected val compositeDisposable = CompositeDisposable()

    protected abstract fun initView()
    protected abstract fun subscribe()
    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        loadingDialog = LoadingDialog.newInstance()
        initView()


    }

    override fun onStart() {
        super.onStart()
        subscribe()
        initData()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    protected fun showLoadingDialog() {
        loadingDialog.show(supportFragmentManager, loadingDialog.tag)
    }

    protected fun hideLoadingDialog() {
        if (loadingDialog.isAdded) {
            loadingDialog.dismissAllowingStateLoss()
        }
    }

    protected fun checkPermissions(permissions: Array<String>): Boolean {
        val result = permissions.map { this.checkSelfPermission(it) }

        if (result.all { it != PackageManager.PERMISSION_GRANTED }) {
            requestPermissions(permissions, 0)

            if (permissions.map { this.checkSelfPermission(it) }
                    .all { it != PackageManager.PERMISSION_GRANTED })
                return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (grantResults.isEmpty()) return
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) return


        val result =
            permissions.map { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }

        // 거부만 선택한 경우
        if (!result.all { true }) {
            showErrorSnackBar(binding.root, "서비스 이용을 위해 권한이 필요합니다.")
            requestPermissions(permissions, 0)
            return
        }

        // 다시 보지 않음까지 선택한 경우
        showLoadingDialog()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showDialogGoToSetting() {
        AlertDialog.Builder(this)
            .setTitle("권한설정")
            .setMessage("원활한 서비스 이용을 위해 권한이 필요합니다. 확인을 눌러 권한 설정 창으로 이동한 뒤 권한 허용 설정을 완료해 주세요.")
            .setPositiveButton("확인") { dialog, i ->
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", baseContext.packageName, null)
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            }
            .setNegativeButton("취소") { dialog, i -> dialog.dismiss() }
            .show()
    }


}