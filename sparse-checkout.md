Clone một phần repo với sparse-checkout
Khi sử dụng Git để quản lý source code, sẽ có những lúc chúng ta cần checkout về một phần nhỏ của repo, thay vì toàn bộ repo đó. Ví dụ khi làm việc trên một monorepo rất to, nhưng bạn chỉ cần phụ trách một module rất nhỏ.

Lấy ví dụ một monorepo dùng chung cho cả backend, frontend và mobile team, mỗi team có 3 project là lemon, orange và watermelon, cấu trúc như sau:

├── backend
│   ├── lemon
│   ├── orange
│   └── watermelon
├── frontend
│   ├── lemon-ui
│   ├── orange-ui
│   └── watermelon-ui
└── mobile
    ├── lemon-app
    ├── orange-app
    └── watermelon-app
Lưu ý khi mình nói monorepo, thì nó là monorepo, tất cả mọi project đều thuộc cùng 1 git repository chứ không có submodule gì hết nhé.

Bạn là một frontend developer thuộc team orange, để làm việc thì bạn chỉ cần 2 thư mục backend/orange và frontend/orange-ui. Không có lý do gì để bạn phải clone toàn bộ repository trên. Để rồi mỗi lần pull code là phải ngồi chờ git nó pull luôn cả những project mà mình không cần làm.

Đầu tiên, chúng ta clone repository này về, và thêm vào option --no-checkout để lấy về tracking info của repo chứ không lấy về bất cứ file/folder nào cả.

$ git clone --no-checkout git@github.com:thepokemoncompany/secret-project
Lúc này, nếu truy cập vào thư mục secret-project, bạn sẽ không thấy nội dung gì cả.

Tiếp theo, sử dụng lệnh git sparse-checkout set để chỉ định các thư mục muốn pull về:

$ git sparse-checkout set /backend/orange /frontend/orange-ui
Kiểm tra danh sách các sparse checkout bằng lệnh:

$ git sparse-checkout list
Bây giờ có thể checkout các file trong list này về bằng lệnh:

$ git checkout
Lúc này bạn sẽ thấy 2 folder backend và frontend , bên trong mỗi folder sẽ chỉ có project orange.

Khi gõ lệnh git status thì bạn sẽ thấy nội dung kiểu như này:

On branch master
Your branch is up to date with 'origin/master'.

**You are in a sparse checkout with 7% of tracked files present.**

nothing to commit, working tree clean
Chứng tỏ hiện tại chúng ta chỉ đang có khoảng 7% nội dung project trên working copy này.

Lưu ý là chức năng sparse-checkout chỉ có từ phiên bản git v2.26.0 trở lên.

