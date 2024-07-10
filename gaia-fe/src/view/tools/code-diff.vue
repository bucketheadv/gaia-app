<template>
  <div class="codeDiff">
    <el-form>
      <el-row>
        <el-col span="12">
          <el-form-item label="Left">
            <el-input type="textarea" v-model="oldStr" rows="12" />
          </el-form-item>
        </el-col>
        <el-col span="12">
          <el-form-item label="Right">
            <el-input type="textarea" v-model="newStr" rows="12" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <vue-code-diff :old-string="oldStr"
                   :new-string="newStr"
                   :content="10"
                   outputFormat="side-by-side"
                   :isShowNoChange="true"
    />
  </div>
</template>
<script lang="js">
import VueCodeDiff from 'vue-code-diff'
export default {
  components: { VueCodeDiff },
  data () {
    return {
      oldStr: 'abc',
      newStr: 'cde'
    }
  }
}
</script>
<style lang="less" scoped>
.versionComparison{
  height: 671px;
  padding: 20px;
  line-height: 1;
  .codeDiff{
    width:1127px;
    height: 300px;
  }
}
</style>
