<template>
  <v-card>
      <v-flex xs12 sm10>
        <v-tree url="/item/category/list"
                :isEdit="isEdit"
                @handleAdd="handleAdd"
                @handleEdit="handleEdit"
                @handleDelete="handleDelete"
                @handleClick="handleClick"
        />
      </v-flex>
  </v-card>
</template>

<script>
  import {treeData} from "../../mockDB";

  export default {
    name: "category",
    data() {
      return {
        isEdit:true,
        treeData
      }
    },
    methods: {
      handleAdd(node) {
        console.log("add .... "+node);
        this.isEdit = false;
        this.$http({
          method: this.isEdit ? 'put' : 'post',
          url: '/item/category/add',
          data:  node
        }).then(() => {
          this.$message.success("添加分类节点成功！");

        }).catch(() => {
            this.$message.error("添加分类节点失败！");
          });
        this.isEdit = true;
      },
      handleEdit(id, name) {
        console.log("edit... id: " + id + ", name: " + name)

        this.isEdit = true;
        this.$http({
          method: this.isEdit ? 'put' : 'post',
          url: '/item/category/edit',
          data:  {"id":id,"name":name}
        }).then(() => {
          this.$message.success("修改分类节点成功！");
        })
          .catch(() => {
            this.$message.error("修改分类节点失败！");
          });

      },
      handleDelete(id) {
        this.$http({
          method: 'get',
          url: '/item/category/del/'+id,
        }).then(() => {
          this.$message.success("删除分类节点成功！");
        })
          .catch(() => {
            this.$message.error("删除分类节点失败！");
          });
      },
      handleClick(node) {
        console.log(node)
      }
    }
  };
</script>

<style scoped>

</style>
