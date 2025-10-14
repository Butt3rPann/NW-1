import { createRouter, createWebHistory } from "vue-router";
import SaleItemsGallery from "@/pages/SaleItemGallery.vue";
import SaleItemsDetail from "@/pages/SaleItemsDetail.vue";
import Homepage from "@/pages/Homepage.vue";
import AddSaleItemForm from "@/components/sale-item/AddSaleItemForm.vue";
import EditSaleItemForm from "@/components/sale-item/EditSaleItemForm.vue";
import SaleItemList from "@/pages/SaleItemList.vue";
import AddBrandForm from "@/components/brand/AddBrandForm.vue";
import BrandList from "@/pages/BrandList.vue";
import EditBrandForm from "@/components/brand/EditBrandForm.vue";
import RegisterForm from "@/components/form/RegisterForm.vue";
import VerifyEmail from "@/pages/VerifyEmail.vue";
import SignIn from "../pages/Signin.vue";
import Profile from "@/pages/Profile.vue";
import EditProfileForm from "@/components/form/EditProfileForm.vue";
import { useUserStore } from "@/stores/UserStore";
import Cart from "@/pages/Cart.vue";
import OrderList from "@/pages/OrderList.vue";
import OrderDetail from "@/pages/OrderDetail.vue";
import SellerOrderList from "@/pages/SellerOrderList.vue";
import ChangePasswordForm from "@/components/form/ChangePasswordForm.vue";
import ResetPassword from "@/pages/ResetPassword.vue";
import ForgetPasswordForm from "@/components/form/ForgetPasswordForm.vue";

const history = createWebHistory('/nw1/')
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
        component: AddSaleItemForm,
        beforeEnter: (to, from) => {
            const userStore = useUserStore()
            if (userStore.getUserType() !== "SELLER") {
                return { name: 'SaleItems' }
            }
        }
    },
    {
        path: '/sale-items/:id/edit',
        name: 'EditSaleItem',
        component: EditSaleItemForm,
        beforeEnter: (to, from) => {
            const userStore = useUserStore()
            if (userStore.getUserType() !== "SELLER") {
                return { name: 'SaleItems' }
            }
        }
    },
    {
        path: '/sale-items/list',
        name: 'SaleItemsList',
        component: SaleItemList,
        beforeEnter: (to, from) => {
            const userStore = useUserStore()
            if (!userStore.isLoggedIn()) {
                return { name: 'SignIn' }
            } else if (userStore.getUserType() === "BUYER") {
                return { name: 'SaleItems' }
            }
        }
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
        component: EditBrandForm
    },
    {
        path : '/registers',
        name : 'Registers',
        component: RegisterForm
    },
    {
        path: '/verify-email',
        name: 'VerifyEmail',
        component: VerifyEmail
    },
    {
        path: '/signin',
        name: 'SignIn',
        component: SignIn
    },
    {
        path: '/profile',
        name: 'Profile',
        component: Profile
    },
    {
        path: '/profile/edit',
        name: 'EditProfile',
        component: EditProfileForm
    },
    {
        path: '/cart',
        name: 'Cart',
        component: Cart
    },
    {
        path: '/your-orders',
        name: 'OrderList',
        component: OrderList
    },
    {
        path: '/your-orders/:orderId',
        name: 'OrderDetail',
        component: OrderDetail
    },
    {
        path: '/sale-orders',
        name: 'SaleOrderList',
        component: SellerOrderList,
        beforeEnter: (to, from) => {
            const userStore = useUserStore()
            if (!userStore.isLoggedIn()) {
                return { name: 'SignIn' }
            } else if (userStore.getUserType() === "BUYER") {
                return { name: 'SaleItems' }
            }
        }
    },
    {
        path: '/change-password',
        name: 'ChangePassword',
        component: ChangePasswordForm
    },
    {
        path: '/forget-password',
        name: 'ForgetPassword',
        component: ForgetPasswordForm
    },
    {
        path: '/reset-password',
        name: 'ResetPassword',
        component: ResetPassword,
        beforeEnter: (to, from, next) => {
        if (to.query.token) {
          next();
        } else {
          next('/forget-password')
        }
      }
    }
]
const router = createRouter({history,routes})

router.beforeEach((to, from) => {
    const isLoggedIn = !!localStorage.getItem('access_token')
    if (!isLoggedIn && (to.name === 'Profile' || 
                        to.name === 'Cart' ||
                        to.name === 'EditProfile')) {
        return { name: 'SignIn' }
    }
})

export default router