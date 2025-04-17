/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
import type {PropsWithChildren} from 'react';
import {
  StatusBar,
  Text,
  useColorScheme,
  View,
  SafeAreaView,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';
import PipScreen from './src/pipScreen/PipScreen';

type SectionProps = PropsWithChildren<{
  title: string;
}>;

function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const safePadding = '5%';

  return (
    <View style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <SafeAreaView>
        <View
          style={{
            backgroundColor: '#F5F5DC',
            padding: safePadding,
            paddingBottom: safePadding,
          }}>
          <Text style={{textAlign: 'center', fontWeight: 'bold', fontSize: 18}}>
            Picture in Picture Native Android Impementation
          </Text>
        </View>

        <View
          style={{
            backgroundColor: Colors.white,
            padding: safePadding,
            paddingBottom: safePadding,
            justifyContent: 'center',
          }}>
          <PipScreen />
        </View>
      </SafeAreaView>
    </View>
  );
}

export default App;
