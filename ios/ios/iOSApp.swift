import SwiftUI
import shared

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
            ComposeViewControllerToSwiftUI().ignoresSafeArea(.all, edges: .bottom)
        }
	}
}

struct ComposeViewControllerToSwiftUI: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        BooksAppViewControllerKt.BooksAppViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
