<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class Form1
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Me.PvForm1 = New openPI_Launcher.PVForm()
        Me.PvProgressBar1 = New openPI_Launcher.PVProgressBar()
        Me.PvButton1 = New openPI_Launcher.PVButton()
        Me.PvButton2 = New openPI_Launcher.PVButton()
        Me.PvButton3 = New openPI_Launcher.PVButton()
        Me.WebBrowser1 = New System.Windows.Forms.WebBrowser()
        Me.PvButton4 = New openPI_Launcher.PVButton()
        Me.PvButton5 = New openPI_Launcher.PVButton()
        Me.PvButton6 = New openPI_Launcher.PVButton()
        Me.PvButton7 = New openPI_Launcher.PVButton()
        Me.PvForm1.SuspendLayout()
        Me.SuspendLayout()
        '
        'PvForm1
        '
        Me.PvForm1.BackColor = System.Drawing.Color.FromArgb(CType(CType(21, Byte), Integer), CType(CType(23, Byte), Integer), CType(CType(25, Byte), Integer))
        Me.PvForm1.Controls.Add(Me.PvButton7)
        Me.PvForm1.Controls.Add(Me.PvButton6)
        Me.PvForm1.Controls.Add(Me.PvButton5)
        Me.PvForm1.Controls.Add(Me.PvButton4)
        Me.PvForm1.Controls.Add(Me.WebBrowser1)
        Me.PvForm1.Controls.Add(Me.PvButton3)
        Me.PvForm1.Controls.Add(Me.PvButton2)
        Me.PvForm1.Controls.Add(Me.PvButton1)
        Me.PvForm1.Controls.Add(Me.PvProgressBar1)
        Me.PvForm1.Dock = System.Windows.Forms.DockStyle.Fill
        Me.PvForm1.Font = New System.Drawing.Font("Segoe UI", 10.0!)
        Me.PvForm1.ForeColor = System.Drawing.Color.FromArgb(CType(CType(146, Byte), Integer), CType(CType(149, Byte), Integer), CType(CType(152, Byte), Integer))
        Me.PvForm1.Location = New System.Drawing.Point(0, 0)
        Me.PvForm1.MinimumSize = New System.Drawing.Size(305, 150)
        Me.PvForm1.Name = "PvForm1"
        Me.PvForm1.Size = New System.Drawing.Size(837, 420)
        Me.PvForm1.TabIndex = 0
        Me.PvForm1.Text = "openPI - Launcher"
        '
        'PvProgressBar1
        '
        Me.PvProgressBar1.BackColor = System.Drawing.Color.FromArgb(CType(CType(21, Byte), Integer), CType(CType(23, Byte), Integer), CType(CType(25, Byte), Integer))
        Me.PvProgressBar1.Font = New System.Drawing.Font("Trebuchet MS", 10.0!)
        Me.PvProgressBar1.ForeColor = System.Drawing.Color.FromArgb(CType(CType(146, Byte), Integer), CType(CType(149, Byte), Integer), CType(CType(152, Byte), Integer))
        Me.PvProgressBar1.Location = New System.Drawing.Point(277, 353)
        Me.PvProgressBar1.Maximum = 100
        Me.PvProgressBar1.Minimum = 0
        Me.PvProgressBar1.MinimumSize = New System.Drawing.Size(21, 21)
        Me.PvProgressBar1.Name = "PvProgressBar1"
        Me.PvProgressBar1.Size = New System.Drawing.Size(547, 55)
        Me.PvProgressBar1.TabIndex = 0
        Me.PvProgressBar1.Text = "PvProgressBar1"
        Me.PvProgressBar1.Value = 50
        '
        'PvButton1
        '
        Me.PvButton1.BackColor = System.Drawing.Color.FromArgb(CType(CType(21, Byte), Integer), CType(CType(23, Byte), Integer), CType(CType(25, Byte), Integer))
        Me.PvButton1.Font = New System.Drawing.Font("Trebuchet MS", 10.0!)
        Me.PvButton1.ForeColor = System.Drawing.Color.FromArgb(CType(CType(146, Byte), Integer), CType(CType(149, Byte), Integer), CType(CType(152, Byte), Integer))
        Me.PvButton1.Location = New System.Drawing.Point(779, 3)
        Me.PvButton1.MinimumSize = New System.Drawing.Size(20, 20)
        Me.PvButton1.Name = "PvButton1"
        Me.PvButton1.Size = New System.Drawing.Size(33, 20)
        Me.PvButton1.TabIndex = 1
        Me.PvButton1.Text = "x"
        '
        'PvButton2
        '
        Me.PvButton2.BackColor = System.Drawing.Color.FromArgb(CType(CType(21, Byte), Integer), CType(CType(23, Byte), Integer), CType(CType(25, Byte), Integer))
        Me.PvButton2.Font = New System.Drawing.Font("Trebuchet MS", 10.0!)
        Me.PvButton2.ForeColor = System.Drawing.Color.FromArgb(CType(CType(146, Byte), Integer), CType(CType(149, Byte), Integer), CType(CType(152, Byte), Integer))
        Me.PvButton2.Location = New System.Drawing.Point(740, 3)
        Me.PvButton2.MinimumSize = New System.Drawing.Size(20, 20)
        Me.PvButton2.Name = "PvButton2"
        Me.PvButton2.Size = New System.Drawing.Size(33, 20)
        Me.PvButton2.TabIndex = 2
        Me.PvButton2.Text = "_"
        '
        'PvButton3
        '
        Me.PvButton3.BackColor = System.Drawing.Color.FromArgb(CType(CType(21, Byte), Integer), CType(CType(23, Byte), Integer), CType(CType(25, Byte), Integer))
        Me.PvButton3.Font = New System.Drawing.Font("Trebuchet MS", 10.0!)
        Me.PvButton3.ForeColor = System.Drawing.Color.FromArgb(CType(CType(146, Byte), Integer), CType(CType(149, Byte), Integer), CType(CType(152, Byte), Integer))
        Me.PvButton3.Location = New System.Drawing.Point(12, 353)
        Me.PvButton3.MinimumSize = New System.Drawing.Size(20, 20)
        Me.PvButton3.Name = "PvButton3"
        Me.PvButton3.Size = New System.Drawing.Size(259, 37)
        Me.PvButton3.TabIndex = 4
        Me.PvButton3.Text = "Download"
        '
        'WebBrowser1
        '
        Me.WebBrowser1.AllowNavigation = False
        Me.WebBrowser1.AllowWebBrowserDrop = False
        Me.WebBrowser1.IsWebBrowserContextMenuEnabled = False
        Me.WebBrowser1.Location = New System.Drawing.Point(12, 72)
        Me.WebBrowser1.MinimumSize = New System.Drawing.Size(20, 20)
        Me.WebBrowser1.Name = "WebBrowser1"
        Me.WebBrowser1.ScriptErrorsSuppressed = True
        Me.WebBrowser1.ScrollBarsEnabled = False
        Me.WebBrowser1.Size = New System.Drawing.Size(259, 275)
        Me.WebBrowser1.TabIndex = 5
        '
        'PvButton4
        '
        Me.PvButton4.BackColor = System.Drawing.Color.FromArgb(CType(CType(21, Byte), Integer), CType(CType(23, Byte), Integer), CType(CType(25, Byte), Integer))
        Me.PvButton4.Font = New System.Drawing.Font("Trebuchet MS", 10.0!)
        Me.PvButton4.ForeColor = System.Drawing.Color.FromArgb(CType(CType(146, Byte), Integer), CType(CType(149, Byte), Integer), CType(CType(152, Byte), Integer))
        Me.PvButton4.Location = New System.Drawing.Point(277, 320)
        Me.PvButton4.MinimumSize = New System.Drawing.Size(20, 20)
        Me.PvButton4.Name = "PvButton4"
        Me.PvButton4.Size = New System.Drawing.Size(131, 27)
        Me.PvButton4.TabIndex = 6
        Me.PvButton4.Text = "Homepage"
        '
        'PvButton5
        '
        Me.PvButton5.BackColor = System.Drawing.Color.FromArgb(CType(CType(21, Byte), Integer), CType(CType(23, Byte), Integer), CType(CType(25, Byte), Integer))
        Me.PvButton5.Font = New System.Drawing.Font("Trebuchet MS", 10.0!)
        Me.PvButton5.ForeColor = System.Drawing.Color.FromArgb(CType(CType(146, Byte), Integer), CType(CType(149, Byte), Integer), CType(CType(152, Byte), Integer))
        Me.PvButton5.Location = New System.Drawing.Point(414, 320)
        Me.PvButton5.MinimumSize = New System.Drawing.Size(20, 20)
        Me.PvButton5.Name = "PvButton5"
        Me.PvButton5.Size = New System.Drawing.Size(131, 27)
        Me.PvButton5.TabIndex = 7
        Me.PvButton5.Text = "Vote"
        '
        'PvButton6
        '
        Me.PvButton6.BackColor = System.Drawing.Color.FromArgb(CType(CType(21, Byte), Integer), CType(CType(23, Byte), Integer), CType(CType(25, Byte), Integer))
        Me.PvButton6.Font = New System.Drawing.Font("Trebuchet MS", 10.0!)
        Me.PvButton6.ForeColor = System.Drawing.Color.FromArgb(CType(CType(146, Byte), Integer), CType(CType(149, Byte), Integer), CType(CType(152, Byte), Integer))
        Me.PvButton6.Location = New System.Drawing.Point(551, 320)
        Me.PvButton6.MinimumSize = New System.Drawing.Size(20, 20)
        Me.PvButton6.Name = "PvButton6"
        Me.PvButton6.Size = New System.Drawing.Size(131, 27)
        Me.PvButton6.TabIndex = 8
        Me.PvButton6.Text = "Highscores"
        '
        'PvButton7
        '
        Me.PvButton7.BackColor = System.Drawing.Color.FromArgb(CType(CType(21, Byte), Integer), CType(CType(23, Byte), Integer), CType(CType(25, Byte), Integer))
        Me.PvButton7.Font = New System.Drawing.Font("Trebuchet MS", 10.0!)
        Me.PvButton7.ForeColor = System.Drawing.Color.FromArgb(CType(CType(146, Byte), Integer), CType(CType(149, Byte), Integer), CType(CType(152, Byte), Integer))
        Me.PvButton7.Location = New System.Drawing.Point(693, 320)
        Me.PvButton7.MinimumSize = New System.Drawing.Size(20, 20)
        Me.PvButton7.Name = "PvButton7"
        Me.PvButton7.Size = New System.Drawing.Size(131, 27)
        Me.PvButton7.TabIndex = 9
        Me.PvButton7.Text = "Donate"
        '
        'Form1
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(837, 420)
        Me.Controls.Add(Me.PvForm1)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None
        Me.MinimumSize = New System.Drawing.Size(305, 150)
        Me.Name = "Form1"
        Me.Text = "Form1"
        Me.TransparencyKey = System.Drawing.Color.Fuchsia
        Me.PvForm1.ResumeLayout(False)
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents PvForm1 As openPI_Launcher.PVForm
    Friend WithEvents PvButton1 As openPI_Launcher.PVButton
    Friend WithEvents PvProgressBar1 As openPI_Launcher.PVProgressBar
    Friend WithEvents PvButton2 As openPI_Launcher.PVButton
    Friend WithEvents WebBrowser1 As System.Windows.Forms.WebBrowser
    Friend WithEvents PvButton3 As openPI_Launcher.PVButton
    Friend WithEvents PvButton7 As openPI_Launcher.PVButton
    Friend WithEvents PvButton6 As openPI_Launcher.PVButton
    Friend WithEvents PvButton5 As openPI_Launcher.PVButton
    Friend WithEvents PvButton4 As openPI_Launcher.PVButton

End Class
