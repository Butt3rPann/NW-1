import { createRouter, createWebHistory } from "vue-router";
import SaleItemsGallery from "@/pages/SaleItemGallery.vue";
import SaleItemsDetail from "@/pages/SaleItemsDetail.vue";
import Homepage from "@/pages/Homepage.vue";
import AddSaleItemForm from "@/components/form/AddSaleItemForm.vue";
import EditSaleItemForm from "@/components/form/EditSaleItemForm.vue";
import SaleItemList from "@/pages/SaleItemList.vue";
import AddBrandForm from "@/components/form/AddBrandForm.vue";
import BrandList from "@/pages/BrandList.vue";

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
        component: SaleItemsGallery
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
    {
        path: '/sale-items/list',
        name: 'SaleItemsList',
        component: SaleItemList
    },
    {
        path: '/brands',
        name: 'BrandList',
        component: BrandList
    },
    {
        path: '/brands/add',
        name: 'AddBrand',
        component: AddBrandForm
    },
    {
        path: '/brands/:id/edit',
        name: 'EditBrand',
        component: BrandList
    }
]
const router = createRouter({history,routes})
export default router