\title{Progetto-03 - Smart Dumpster}

Si vuole realizzare un sistema IoT che implementi una versione semplificata di uno \textbf{smart dumpster}, ovvero un cassonetto dei rifiuti “intelligente”.

All'interno della cartella _src_ troviamo il codice così organizzato:
\begin{itemize}
	\item \textit{ds-controller} contiene il codice wiring per Arduino UNO.
	\item \textit{ds-mobile-app} contiene il codice per l'app Android che controlla lo Smart Dumpster.
	\item \textit{ds-edge} contiene il codice wiring per ESP8266.
	\item \textit{ds-service} contiene il codice per la REST API.
	\item \textit{ds-dashboard} contiene il codice per il client.
\end{itemize}

All'interno della cartella \textit{doc} vi è una breve relazione del progetto.

All'interno della cartella \textit{res} sono contenuti:
\begin{itemize}
	\item architettura del dumpster
	\item diagrammi delle macchine a stati finiti di \textit{controller} e \textit{edge}
	\item schemi Fritzing di \textit{controller} e \textit{edge}
	\item screenshot di \textit{mobile-app} e \textit{dashboard}
\end{itemize}