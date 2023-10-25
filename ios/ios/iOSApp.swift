import SwiftUI
import shared

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
            ComposeViewControllerToSwiftUI()
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