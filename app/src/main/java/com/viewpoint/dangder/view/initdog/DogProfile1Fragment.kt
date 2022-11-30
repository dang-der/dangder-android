package com.viewpoint.dangder.view.initdog

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDogProfile1Binding
import com.viewpoint.dangder.util.ImageOrderMover
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.view.adapter.ImageListAdapter
import com.viewpoint.dangder.viewmodel.RegisterDogViewModel
import timber.log.Timber
import java.text.SimpleDateFormat


class DogProfile1Fragment : BaseFragment<FragmentDogProfile1Binding>() {
    override val layoutId: Int
        get() = R.layout.fragment_dog_profile_1

    private val registerDogViewModel: RegisterDogViewModel by hiltNavGraphViewModels(R.id.init_dog_nav_graph)

    private lateinit var imageDialog :BottomSheetDialog
    private val imageListAdapter = ImageListAdapter()
    private var cameraImageUri:Uri? = null

    // 갤러리에서 이미지 가져오는 launcher
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode != RESULT_OK) return@registerForActivityResult
        val imageUri = it.data?.data
        imageUri?.let {
            imageListAdapter.addItem(it)
        }
    }

    // 카메라에서 이미지 가져오는 launcher
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode != RESULT_OK) return@registerForActivityResult
        cameraImageUri?.let {
            imageListAdapter.addItem(it)
        }
    }


    override fun subscribeModel() {

    }

    override fun initData() {}

    override fun initView() {
        requestPermissions(arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        ))

        initImageDialog()
        initImageListView()

        handleClickAddImage()
        handleClickNext()
    }

    private fun handleClickAddImage(){
        binding.initdogAddImageBtn.setOnClickListener {

            if(imageListAdapter.itemCount >=3){
                showErrorSnackBar(binding.root, "이미지는 최대 3장까지 등록 가능합니다.")
                return@setOnClickListener
            }

            val permissionResult = checkPermissions(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                )
            )

            if(permissionResult.not()) {
                showDialogGoToSetting(arrayOf("저장소", "카메라"))
                return@setOnClickListener
            }

            imageDialog.show()
        }
    }

    private fun handleClickNext(){
        binding.initdogNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_dogProfile1Fragment_to_dogProfile2Fragment)
        }
    }

    private fun initImageDialog(){
        val imageDialogView = layoutInflater.inflate(R.layout.dialog_select_gallery_camera, null)
        imageDialog = BottomSheetDialog(requireActivity()).apply {
            setContentView(imageDialogView)
        }

        imageDialogView.findViewById<TextView>(R.id.dialog_image_gallery_btn).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            }
            galleryLauncher.launch(intent)
            imageDialog.dismiss()
        }

        imageDialogView.findViewById<TextView>(R.id.dialog_image_camera_btn).setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            createImageUri(generateFileName(), "image/jpg")?.let {
                cameraImageUri = it
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
                cameraLauncher.launch(intent)
                imageDialog.dismiss()
            }

        }
    }

    private fun createImageUri (fileName : String, mimeType : String) : Uri?{
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        }
        return requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun generateFileName(): String {
        val fileName = SimpleDateFormat("yyyyMMdd-HHmmss").let {
            it.format(System.currentTimeMillis())
        }
        return "${fileName}.jpg"
    }

    private fun initImageListView(){
        binding.initdogImageList.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.initdogImageList.adapter = imageListAdapter

        val moveCallback = ImageOrderMover { from, to -> imageListAdapter.swapItem(from, to) }
        val touchHelper = ItemTouchHelper(moveCallback)

        touchHelper.attachToRecyclerView(binding.initdogImageList)

        imageListAdapter.onStartDragListener = object :ImageListAdapter.OnStartDragListener{
            override fun onStartDrag(viewHolder: ImageListAdapter.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        }
    }

}