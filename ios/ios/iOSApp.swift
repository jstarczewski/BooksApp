import SwiftUI
import shared
import BackgroundTasks

@main
struct iOSApp: App {
    @Environment(\.scenePhase) private var phase
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
        WindowGroup {
            ComposeViewControllerToSwiftUI()
                .ignoresSafeArea(.all, edges: .bottom)
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



