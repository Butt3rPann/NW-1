import { createRouter, createWebHistory } from "vue-router";
import SaleItemList from "@/pages/SaleItemList.vue";
import SaleItemsDetail from "@/pages/SaleItemsDetail.vue";
import Homepage from "@/pages/Homepage.vue";
import AddSaleItemForm from "@/components/form/AddSaleItemForm.vue";
import EditSaleItemForm from "@/components/form/EditSaleItemForm.vue";

const history = createWebHistory()
const routes = [
    {
        path: '/',
        name: 'Homepage',
        component: Homepage
    },
    {
        path: '/sale-items',
        name: 'SaleItems',
        component: SaleItemList
    },
    {
        path: '/sale-items/:saleItemId',
        name: 'SaleItemsDetail',
        component: SaleItemsDetail
    },
    {
        path: '/sale-items/add',
        name: 'AddSaleItem',
        component: AddSaleItemForm
    },
    {
        path: '/sale-items/:id/edit',
        name: 'EditSaleItem',
        component: EditSaleItemForm
    },
]
const router = createRouter({history,routes})
export default router