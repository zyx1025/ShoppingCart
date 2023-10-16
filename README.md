# ShoppingCart
小组开发练习，我负责安卓端，通过后端同学负责提供的接口接收json格式数据/发送响应
例如我从xx.xx.xx.xx【在network目录下的ApiService中的BASE_URL字段中写明，我在这里把该链接隐藏】/goods获取以下数据：{
{
    "code": 7,
    "message": "There are 7 item(s) in total.",
    "data": [
        {
            "id": 1,
            "name": "红星照耀中国",
            "price": 69,
            "image": "https://img.alicdn.com/imgextra/i4/4128707838/O1CN010hO1mz27lrErdsUta_!!0-saturn_solar.jpg_468x468q75.jpg_.webp"
        },
        {
            "id": 2,
            "name": "C++ Primer Plus",
            "price": 120,
            "image": "https://img.alicdn.com/imgextra/i2/14789935/O1CN0109Ijp32NGHc3sGsG4_!!0-saturn_solar.jpg_468x468q75.jpg_.webp"
        },
        {
            "id": 3,
            "name": "Python编程从入门到实践",
            "price": 80,
            "image": "https://img.alicdn.com/imgextrahttps://img.alicdn.com/imgextra/i4/859515618/O1CN01JR0l0b1rN5xkHzE08_!!859515618-0-alimamacc.jpg_468x468q75.jpg_.webp"
        },
        {
            "id": 4,
            "name": "Web API的设计与开发",
            "price": 52,
            "image": "https://g-search1.alicdn.com/img/bao/uploaded/i4/i3/2145487409/O1CN015MJjJc24bNFe3OZlT_!!0-item_pic.jpg_360x360q90.jpg_.webp"
        },
        {
            "id": 5,
            "name": "JavaScript权威指南",
            "price": 99,
            "image": "https://picasso.alicdn.com/imgextra/O1CNA1Vi8v1H1CP1OuZfmdq_!!101450072-0-psf.jpg_360x360q90.jpg_.webp"
        },
        {
            "id": 6,
            "name": "Vue前端开发指南",
            "price": 35,
            "image": "https://picasso.alicdn.com/imgextra/O1CNA1VOamnF1Tn9gLutEEV_!!2041592426-0-psf.jpg_360x360q90.jpg_.webp"
        },
        {
            "id": 7,
            "name": "Go Web编程",
            "price": 128,
            "image": "https://g-search3.alicdn.com/img/bao/uploaded/i4/i1/1932014659/O1CN01cNvVtv1kHrxQ6mQuq_!!0-item_pic.jpg_580x580q90.jpg_.webp"
        }
    ]
}，页面渲染如下：![Uploading 6A41B7FAA59C8BB4C4AC52B8BBD4EB71.jpg…]()

