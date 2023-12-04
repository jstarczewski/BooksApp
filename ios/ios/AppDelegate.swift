//
//  AppDelegate.swift
//  ios
//
//  Created by Jan Starczewski on 02/12/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared
import BackgroundTasks

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        Setup_iosKt.setup()
        return true
    }
}
