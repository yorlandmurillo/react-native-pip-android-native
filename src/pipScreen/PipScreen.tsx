import {useEffect} from 'react';
import {
  NativeEventEmitter,
  NativeModules,
  AppState,
  View,
  Text,
  TouchableOpacity,
  ImageBackground,
} from 'react-native';

const PipScreen = () => {
  // If you need more control you can create a Context to allow navigation within your app
  const {NativePipModule} = NativeModules;
  // Event emitter to know when the user exits the PiP window
  const pipEventEmitter = new NativeEventEmitter(NativePipModule);

  useEffect(() => {
    const exitSubscription = pipEventEmitter.addListener('onExitPip', () => {
      // If the app is in the background, bring it to the front via native call
      if (AppState.currentState === 'background') {
        NativePipModule.bringAppToFront();
      }
    });

    const destroyedSubscription = pipEventEmitter.addListener(
      'onPipDestroyed',
      () => {
        // Handle PiP activity destruction if needed
        console.log('PiP activity was destroyed');
      },
    );

    return () => {
      exitSubscription.remove();
      destroyedSubscription.remove();
    };
  }, []);

  return (
    <View>
      <TouchableOpacity
        onPress={() => {
          NativePipModule?.showPIP(
            'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
          );
        }}>
        <ImageBackground
          source={require('../../assets/big_bunny_poster.png')}
          resizeMode="cover"
          style={{
            justifyContent: 'center',
            width: '100%',
            height: 200,
          }}
        />
        <Text style={{textAlign: 'center', marginTop: 20}}>
          Click on the image to enter PiP
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default PipScreen;
