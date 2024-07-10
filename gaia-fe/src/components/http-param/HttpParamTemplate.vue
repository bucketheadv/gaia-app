<template>
  <div class="ivu-card-body ivu-card-bordered">
    <Row>
      <Col span="8">
        <el-form-item label="名称" :prop="propName" required>
          <Input type="text" v-model="value.name" />
        </el-form-item>
      </Col>
      <Col span="8">
        <el-form-item label="中文名">
          <Input type="text" v-model="value.nameCN" />
        </el-form-item>
      </Col>
      <Col span="8">
        <el-form-item label="必填">
          <Checkbox v-model="value.required" />
        </el-form-item>
      </Col>
    </Row>
    <Row>
      <Col span="8">
        <el-form-item label="类型">
          <Select v-model="value.type" @on-change="resetDefaultValue(value)">
            <Option value="string">字符串</Option>
            <Option value="long">整数</Option>
            <Option value="select">下拉菜单</Option>
            <Option value="date">日期</Option>
            <Option value="datetime">日期时间</Option>
            <Option value="boolean">布尔值</Option>
          </Select>
        </el-form-item>
      </Col>
      <Col span="8">
        <el-form-item label="默认值">
          <Input type="text" v-model="value.defaultValue" v-if="showString(value)" />
          <InputNumber v-model="value.defaultValue" v-if="showNumber(value)" />
          <el-date-picker :type="value.type" v-model="value.defaultValue" v-if="showDate(value)" style="width: 200px"/>
          <Checkbox v-model="value.defaultValue" v-if="showBoolean(value)" >是</Checkbox>
          <el-select v-model="value.defaultValue" v-if="showSelect(value)">
            <el-option v-for="v in (value.availableValues || '').split('\n')" :key="v" :value="v.split(',')[0]">
              {{v.split(',')[1] || v.split(',')[0]}}
            </el-option>
          </el-select>
        </el-form-item>
      </Col>
      <Col span="8">
        <el-form-item label="禁止修改">
          <Checkbox v-model="value.uneditable" />
        </el-form-item>
      </Col>
    </Row>
    <Row>
      <Col span="8" v-if="value.type === 'select'">
        <el-form-item label="可选值" :required="true">
          <el-input type="textarea" v-model="value.availableValues" placeholder="每行一个值, 可逗号分隔（第1个值为value，第二个值为label）" />
        </el-form-item>
      </Col>
    </Row>
  </div>
</template>

<script lang="js">
export default {
  name: 'HttpParamTemplate',
  props: {
    propName: String,
    value: {
      type: Object
    }
  },
  methods: {
    showString (value) {
      return value.type === 'string'
    },
    showNumber (value) {
      return value.type === 'long'
    },
    showDate (value) {
      return value.type === 'date' || value.type === 'datetime'
    },
    showBoolean (value) {
      return value.type === 'boolean'
    },
    showSelect (value) {
      return value.type === 'select'
    },
    resetDefaultValue (value) {
      value.defaultValue = null
    }
  }
}
</script>

<style lang="less">
.ivu-input-inner-container {
  padding: 0 10px 0 0;
}
</style>
