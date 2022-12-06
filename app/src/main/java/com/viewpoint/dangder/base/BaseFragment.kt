package com.viewpoint.dangder.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.view.dialog.LoadingDialog
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    protected val compositeDisposable = CompositeDisposable()
    protected lateinit var binding: B
    protected abstract val layoutId: Int
    protected lateinit var loadingDialog: LoadingDialog

    protected abstract fun initView()
    protected abstract fun subscribeModel()
    protected abstract fun initData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        loadingDialog = LoadingDialog.newInstance()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        subscribeModel()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    protected fun showLoadingDialog() {
        loadingDialog.show(requireActivity().supportFragmentManager, loadingDialog.tag)
    }

    protected fun hideLoadingDialog() {
        if (loadingDialog.isAdded) {
            loadingDialog.dismissAllowingStateLoss()
        }
    }

    protected fun requestPermissions(permissions: Array<String>) {
        val result = permissions.map { requireActivity().checkSelfPermission(it) }

        if (result.all { it == PackageManager.PERMISSION_GRANTED }.not()) {
            requireActivity().requestPermissions(permissions, 0)
        }
    }

    protected fun checkPermissions(permissions: Array<String>): Boolean {
        return permissions.map { requireActivity().checkSelfPermission(it) }
            .all { it == PackageManager.PERMISSION_GRANTED }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (grantResults.isEmpty()) return
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) return


        val result =
            permissions.map {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    it
                )
            }

        // 거부만 선택한 경우
        if (!result.all { true }) {
            showErrorSnackBar(binding.root, "서비스 이용을 위해 권한이 필요합니다.")
            requireActivity().requestPermissions(permissions, 0)
            return
        }

        // 다시 보지 않음까지 선택한 경우
        showLoadingDialog()
    }

    protected fun showDialogGoToSetting(permissions: Array<String>) {
        AlertDialog.Builder(requireActivity())
            .setTitle("권한설정")
            .setMessage("원활한 서비스 이용을 위해 ${permissions.joinToString(",")}권한이 필요합니다. 확인을 눌러 권한 설정 창으로 이동한 뒤 권한 허용 설정을 완료해 주세요.")
            .setPositiveButton("확인") { dialog, i ->
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", requireActivity().baseContext.packageName, null)
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            }
            .setNegativeButton("취소") { dialog, i -> dialog.dismiss() }
            .show()
    }


}