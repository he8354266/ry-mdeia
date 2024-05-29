<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="number">
        <el-input v-model="queryParams.number" placeholder="请输入编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="queryParams.nickName" placeholder="请输入昵称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="地点" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="特色" prop="feature">
        <el-input
          v-model="queryParams.feature"
          placeholder="请输入特色"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否空闲 0：空闲   1：空闲" prop="free">
        <el-input
          v-model="queryParams.free"
          placeholder="请输入是否空闲 0：空闲   1：空闲"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否推荐 0：不推荐  1：推荐" prop="suggest">
        <el-input
          v-model="queryParams.suggest"
          placeholder="请输入是否推荐 0：不推荐  1：推荐"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入分类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['waterfall:info:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['waterfall:info:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDeletes"
          v-hasPermi="['waterfall:info:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="number" />
      <el-table-column label="昵称" align="center" prop="nickName" />
      <el-table-column label="介绍" align="center" prop="introduce" />
      <el-table-column label="详情图片" align="center" prop="video">
        <template slot-scope="scope">
          <img style="display:block;width: 100%;height: 100%;" :src="baseUrl + scope.row.detailsImg" />
        </template>
      </el-table-column>
      <el-table-column label="地点" align="center" prop="address" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ry_details_address" :value="scope.row.address" />
        </template>
      </el-table-column>
      <el-table-column label="特色" align="center" prop="feature" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ry_details_feature" :value="scope.row.feature" />
        </template>
      </el-table-column>
      <el-table-column label="是否空闲">
        <template slot-scope="scope">
          <span v-if="scope.row.free"><el-tag type="success">空闲</el-tag></span>
          <span v-else><el-tag type="danger">非空闲</el-tag></span>
        </template>
      </el-table-column>
      <el-table-column label="是否推荐">
        <template slot-scope="scope">
          <span v-if="scope.row.suggest"><el-tag type="success">推荐</el-tag></span>
          <span v-else><el-tag type="danger">不推荐</el-tag></span>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="category" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ry_details_category" :value="scope.row.category" />
        </template>
      </el-table-column>
      <el-table-column label="验证信息" align="center" prop="validateInfo" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.
            ry_validate_category" :value="scope.row.validateInfo" />
        </template>
      </el-table-column>
      <el-table-column label="备注信息" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['waterfall:info:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeletes(scope.row)"
            v-hasPermi="['waterfall:info:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改waterfall对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>

      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="编号" prop="number">
              <el-input v-model="form.number" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="form.nickName" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="介绍" prop="introduce">
              <el-input v-model="form.introduce" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类">
              <el-select v-model="form.category" placeholder="请选择分类" style="width:100%">
                <el-option v-for="dict in dict.type.ry_details_category" :key="dict.value" :label="dict.label"
                  :value="Number(dict.value)" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>


          <el-col :span="12">
            <el-form-item label="地点">
              <el-select v-model="form.address" placeholder="请选择地点" style="width:100%" clearable>
                <el-option v-for="dict in dict.type.ry_details_address" :key="dict.value" :label="dict.label"
                  :value="Number(dict.value)" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="特色">
              <el-select v-model="form.feature" placeholder="请选择特色" style="width:100%">
                <el-option v-for="dict in dict.type.ry_details_feature" :key="dict.value" :label="dict.label"
                  :value="Number(dict.value)" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="是否空闲" prop="free">
              <el-switch v-model="form.free" active-color="#13ce66" inactive-color="#ff4949">
              </el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否推荐" prop="suggest">
              <el-switch v-model="form.suggest" active-color="#13ce66" inactive-color="#ff4949">
              </el-switch>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row>
          <el-col :span="12">
            <el-form-item label="验证信息">
              <el-select v-model="form.validateInfo" placeholder="请选择验证分类" multiple style="width:100%">
                <el-option v-for="dict in dict.type.ry_validate_category" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注信息" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="详情图片" prop="detailsImg">
              <div class="component-upload-image">
                <el-upload multiple :action="uploadImgUrl" list-type="picture-card" :on-success="imgHandleUploadSuccess"
                  :before-upload="imgHandleBeforeUpload" :limit="limit" :on-error="imgHandleUploadError"
                  :on-exceed="imgHandleExceed" ref="imageUpload" :on-remove="imgHandleDelete" :show-file-list="true"
                  :headers="headers" :file-list="imgFileList" :on-preview="handlePictureCardPreview"
                  :class="{ hide: this.imgFileList.length >= this.limit }">
                  <i class="el-icon-plus"></i>
                </el-upload>

                <!-- 上传提示 -->
                <div class="el-upload__tip" slot="tip" v-if="showTip">
                  请上传
                  <template v-if="imgFileSize"> 大小不超过 <b style="color: #f56c6c">{{ imgFileSize }}MB</b> </template>
                  <template v-if="imgFileType"> 格式为 <b style="color: #f56c6c">{{ imgFileType.join("/") }}</b> </template>
                  的文件
                </div>

                <el-dialog :visible.sync="dialogVisible" title="预览" width="800" append-to-body>
                  <img :src="dialogImageUrl" style="display: block; max-width: 100%; margin: 0 auto" />
                </el-dialog>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="详情视频" prop="video">
              <div class="upload-file">
                <el-upload multiple :action="uploadFileUrl" :before-upload="handleBeforeUpload" :file-list="fileList"
                  :limit="limit" :on-error="handleUploadError" :on-exceed="handleExceed" :on-success="handleUploadSuccess"
                  :show-file-list="false" :headers="headers" class="upload-file-uploader" ref="fileUpload">
                  <!-- 上传按钮 -->
                  <el-button size="mini" type="primary">选取视频</el-button>
                  <!-- 上传提示 -->
                  <div class="el-upload__tip" slot="tip" v-if="showTip">
                    请上传
                    <template v-if="fileSize"> 大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b> </template>
                    <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b> </template>
                    的文件
                  </div>
                </el-upload>

                <!-- 文件列表 -->
                <transition-group class="upload-file-list el-upload-list el-upload-list--text" name="el-fade-in-linear"
                  tag="ul">
                  <li :key="file.url" class="el-upload-list__item ele-upload-list__item-content"
                    v-for="(file, index) in fileList">
                    <el-link :href="`${baseUrl}${file.url}`" :underline="false" target="_blank">
                      <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
                    </el-link>
                    <div class="ele-upload-list__item-content-action">
                      <el-link :underline="false" @click="handleDelete(index)" type="danger">删除</el-link>
                    </div>
                  </li>
                </transition-group>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from "@/api/waterfall/info";
import { getToken } from "@/utils/auth";
export default {
  name: "Info",
  dicts: ['ry_details_feature', 'ry_details_address', 'ry_details_category', 'ry_validate_category'],
  props: {
    // 值
    value: [String, Object, Array],
    // 数量限制
    limit: {
      type: Number,
      default: 1,
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 600,
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array,
      default: () => ['mp4', 'avi', 'rmvb', 'wmv']
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    imgFileSize: {
       type: Number,
      default: 20,
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    imgFileType: {
      type: Array,
      default: () => ["png", "jpg", "jpeg"],
    },

  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // waterfall表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        number: null,
        name: null,
        desc: null,
        detailsImg: null,
        address: null,
        feature: null,
        free: null,
        suggest: null,
        video: null,
        category: null,
        validateInfo: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      number: 0,
      uploadList: [],
      imgList: [],
      baseUrl: process.env.VUE_APP_BASE_API,
      uploadFileUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传文件服务器地址
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      fileList: [],


      imgNumber: 0,
      imgUploadList: [],
      dialogImageUrl: "",
      dialogVisible: false,
      hideUpload: false,
      uploadImgUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      imgFileList: []
    };
  },
  watch: {
    value: {
      handler(val) {

        if (val) {
          let temp = 1;
          // 首先将值转为数组
          const list = Array.isArray(val) ? val : this.value.split(',');
          // 然后将数组转为对象数组
          this.fileList = list.map(item => {
            if (typeof item === "string") {
              item = { name: item, url: item };
            }
            item.uid = item.uid || new Date().getTime() + temp++;
            return item;
          });
        } else {
          this.fileList = [];
          return [];
        }
      },
      deep: true,
      immediate: true
    }
  },
  computed: {
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize);
    },

  },
  created() {
    this.getList();
  },
  methods: {
    handleChange(value) {
      var temp = this.dict.type.db_driver.filter(item => item.value === value)[0];
      this.form.dbType = temp.label;
    },
    /** 查询waterfall列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        number: null,
        name: null,
        desc: null,
        detailsImg: null,
        address: null,
        feature: null,
        free: null,
        suggest: null,
        video: null,
        category: null,
        validateInfo: [],
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.fileList =[];
      this.imgFileList =[];
      this.reset();
      this.open = true;
      this.title = "添加waterfall";
      
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getInfo(id).then(response => {
        this.form = response.data;
        this.form.validateInfo = response.data.validateInfo.split(',')
        this.fileList = this.form.video ? [{ url: this.form.video, name: this.form.video }] : []
        this.imgFileList = this.form.detailsImg ? [{ url: this.baseUrl + this.form.detailsImg, name: this.form.detailsImg }] : []
        this.open = true;
        this.title = "修改";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const postParams = JSON.parse(JSON.stringify(this.form))
          postParams.video = this.fileList[0].url
          postParams.validateInfo = postParams.validateInfo.toString()
          postParams.detailsImg = this.imgFileList[0].url.substring(this.baseUrl.length)
          if (this.form.id != null) {
            updateInfo(postParams).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInfo(postParams).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDeletes(row) {
      console.log(11)
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除waterfall编号为"' + ids + '"的数据项？').then(function () {
        return delInfo(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('waterfall/info/export', {
        ...this.queryParams
      }, `info_${new Date().getTime()}.xlsx`)
    },
    // 上传前校检格式和大小
    handleBeforeUpload(file) {
      // 校检文件类型
      if (this.fileType) {
        const fileName = file.name.split('.');
        const fileExt = fileName[fileName.length - 1];
        const isTypeOk = this.fileType.indexOf(fileExt) >= 0;
        if (!isTypeOk) {
          this.$modal.msgError(`文件格式不正确, 请上传${this.fileType.join("/")}格式文件!`);
          return false;
        }
      }
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$modal.msgError(`上传文件大小不能超过 ${this.fileSize} MB!`);
          return false;
        }
      }
      this.$modal.loading("正在上传文件，请稍候...");
      this.number++;
      return true;
    },
    // 文件个数超出
    handleExceed() {
      this.$modal.msgError(`上传文件数量不能超过 ${this.limit} 个!`);
    },
    // 上传失败
    handleUploadError(err) {
      this.$modal.msgError("上传文件失败，请重试");
      this.$modal.closeLoading();
    },
    // 上传成功回调
    handleUploadSuccess(res, file) {
      if (res.code === 200) {
        this.uploadList.push({ name: res.fileName, url: res.fileName });
        console.log(this.uploadList)
        this.uploadedSuccessfully();
      } else {
        this.number--;
        this.$modal.closeLoading();
        this.$modal.msgError(res.msg);
        this.$refs.fileUpload.handleRemove(file);
        this.uploadedSuccessfully();
      }
    },
    // 删除文件
    handleDelete(index) {
      this.fileList.splice(index, 1);
      this.$emit("input", this.listToString(this.fileList));
    },
    // 上传结束处理
    uploadedSuccessfully() {
      if (this.number > 0 && this.uploadList.length === this.number) {
        this.fileList = this.fileList.concat(this.uploadList);
        this.uploadList = [];
        this.number = 0;
        this.$emit("input", this.listToString(this.fileList));
        this.$modal.closeLoading();
      }
    },
    // 获取文件名称
    getFileName(name) {
      // 如果是url那么取最后的名字 如果不是直接返回
      if (name.lastIndexOf("/") > -1) {
        return name.slice(name.lastIndexOf("/") + 1);
      } else {
        return name;
      }
    },
    // 对象转成指定字符串分隔
    listToString(list, separator) {
      let strs = "";
      separator = separator || ",";
      for (let i in list) {
        strs += list[i].url + separator;
      }
      return strs != '' ? strs.substr(0, strs.length - 1) : '';
    }, // 上传前loading加载
    imgHandleBeforeUpload(file) {
      let isImg = false;
      if (this.imgFileType.length) {
        let fileExtension = "";
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
        }
        isImg = this.imgFileType.some(type => {
          if (file.type.indexOf(type) > -1) return true;
          if (fileExtension && fileExtension.indexOf(type) > -1) return true;
          return false;
        });
      } else {
        isImg = file.type.indexOf("image") > -1;
      }

      if (!isImg) {
        this.$modal.msgError(`文件格式不正确, 请上传${this.imgFileType.join("/")}图片格式文件!`);
        return false;
      }
      if (this.imgFileSize) {
        const isLt = file.size / 1024 / 1024 < this.imgFileSize;
        if (!isLt) {
          this.$modal.msgError(`上传头像图片大小不能超过 ${this.imgFileSize} MB!`);
          return false;
        }
      }
      this.$modal.loading("正在上传图片，请稍候...");
      this.number++;
    },
    // 文件个数超出
    imgHandleExceed() {
      this.$modal.msgError(`上传文件数量不能超过 ${this.limit} 个!`);
    },
    // 上传成功回调
    imgHandleUploadSuccess(res, file) {
      if (res.code === 200) {
        this.imgUploadList.push({ name: res.fileName, url: this.baseUrl + res.fileName });
        this.imgUploadedSuccessfully();
      } else {
        this.number--;
        this.$modal.closeLoading();
        this.$modal.msgError(res.msg);
        this.$refs.imgUploadList.handleRemove(file);
        this.imgUploadedSuccessfully();
      }
    },
    // 删除图片
    imgHandleDelete(file) {
      const findex = this.imgFileList.map(f => f.name).indexOf(file.name);
      if (findex > -1) {
        this.imgFileList.splice(findex, 1);
        this.$emit("input", this.listToString(this.imgFileList));
      }
    },
    // 上传失败
    imgHandleUploadError() {
      this.$modal.msgError("上传图片失败，请重试");
      this.$modal.closeLoading();
    },
    // 上传结束处理
    imgUploadedSuccessfully() {
      if (this.number > 0 && this. imgUploadList.length === this.number) {
        this.imgFileList = this.imgFileList.concat(this. imgUploadList);
        this. imgUploadList = [];
        this.number = 0;
        this.$emit("input", this.listToString(this.imgFileList));
        this.$modal.closeLoading();
      }
    },
    // 预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = this.baseUrl+file.url;
      this.dialogVisible = true;
    }
    
  }
};
</script>
