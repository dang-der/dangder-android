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
import com.viewpoint.dangder.presenter.dialog.LoadingDialog
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber


abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: B
    protected abstract val layoutId: Int
    protected val loadingDialog: LoadingDialog = LoadingDialog()
    protected val compositeDisposable = CompositeDisposable()

    protected abstract fun initView()
    protected abstract fun subscribe()
    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
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
        if (loadingDialog.isAdded) return
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

        // ????????? ????????? ??????
        if (!result.all { true }) {
            showErrorSnackBar(binding.root, "????????? ????????? ?????? ????????? ???????????????.")
            requestPermissions(permissions, 0)
            return
        }

        // ?????? ?????? ???????????? ????????? ??????
        showLoadingDialog()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showDialogGoToSetting() {
        AlertDialog.Builder(this)
            .setTitle("????????????")
            .setMessage("????????? ????????? ????????? ?????? ????????? ???????????????. ????????? ?????? ?????? ?????? ????????? ????????? ??? ?????? ?????? ????????? ????????? ?????????.")
            .setPositiveButton("??????") { dialog, i ->
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", baseContext.packageName, null)
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            }
            .setNegativeButton("??????") { dialog, i -> dialog.dismiss() }
            .show()
    }


}